package com.example.mvvm.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvm.data.local.SharedPreferencesTokenProvider
import com.example.mvvm.models.Project
import com.example.mvvm.repositories.AuthRepository
import com.example.mvvm.repositories.MainLog
import com.example.mvvm.repositories.Store
import com.example.mvvm.repositories.apis.keycloak.KeycloakRepository
import com.example.mvvm.repositories.apis.project.ProjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

//data class HomeUiState (
//    val test: String
//)

data class HomeUiState(
    val test: String = "",
    val projects: List<Project> = emptyList(),
    val isLoading: Boolean = false,
    val hasMore: Boolean = true,
    val page: Int = 0
)


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val log: MainLog?,
    private val store: Store?,
    private val repository: ProjectRepository,
    private val keycloakRepository: KeycloakRepository,
    private val tokenProvider: SharedPreferencesTokenProvider,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadProjects()
    }

    private fun loadProjects() {
        viewModelScope.launch {
            try {
                val response = repository.getProjects()
                if (response.isSuccessful) {
                    response.body()?.let { projectResponse ->
                        _uiState.value = _uiState.value.copy(projects = projectResponse.content)
                    } ?: run {
                        log?.e("HomeViewModel", "Response body is null")
                    }
                } else {
                    log?.e("HomeViewModel", "API failed with code: ${response.code()}")
                }
            } catch (e: Exception) {
                log?.e("HomeViewModel", "Failed to load projects: ${e.message}")
            }
        }
    }

    fun loadMoreProjects() {
        if (uiState.value.isLoading || !uiState.value.hasMore) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                val response = repository.getProjects() // chưa có page thì bạn cần thêm page nếu backend hỗ trợ
                if (response.isSuccessful) {
                    val projectResponse = response.body()
                    val newProjects = projectResponse?.content ?: emptyList()
                    _uiState.update {
                        it.copy(
                            projects = it.projects + newProjects,
                            isLoading = false,
                            page = it.page + 1,
                            hasMore = newProjects.isNotEmpty()
                        )
                    }
                } else {
                    log?.e("HomeViewModel", "API failed with code: ${response.code()}")
                    _uiState.update { it.copy(isLoading = false) }
                }
            } catch (e: Exception) {
                log?.e("HomeViewModel", "Failed to load more projects: ${e.message}")
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun logout(onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            val refreshToken = tokenProvider.getRefreshToken()

            if (refreshToken.isNullOrEmpty()) {
                authRepository.logout()
                onComplete(true)
                return@launch
            }

            keycloakRepository.logout(refreshToken).fold(
                onSuccess = {
                    authRepository.logout()
                    onComplete(true)
                },
                onFailure = { error ->
                    // Even if the server-side logout fails, we still clear tokens locally
                    authRepository.logout()
                    onComplete(true)
                }
            )
        }
    }

}

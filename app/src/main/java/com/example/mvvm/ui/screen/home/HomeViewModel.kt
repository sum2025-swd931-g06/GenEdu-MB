package com.example.mvvm.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvm.models.Project
import com.example.mvvm.repositories.MainLog
import com.example.mvvm.repositories.Store
import com.example.mvvm.repositories.apis.project.ProjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

//data class HomeUiState (
//    val test: String
//)

data class HomeUiState(
    val test: String = "",
    val projects: List<Project> = emptyList()
)


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val log: MainLog?,
    private val store: Store?,
    private val repository: ProjectRepository // giả sử bạn có ProjectRepository
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
                    response.body()?.let { projectList ->
                        _uiState.value = _uiState.value.copy(projects = projectList)
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

}

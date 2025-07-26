package com.example.mvvm.ui.screen.project

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvm.models.FinalizedLecture
import com.example.mvvm.models.Project
import com.example.mvvm.repositories.FinalizedLectureRepository
import com.example.mvvm.repositories.apis.RetrofitInstance
import com.example.mvvm.repositories.apis.project.ProjectRepository
import com.example.mvvm.security.TokenProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProjectViewModel @Inject constructor(
    private val projectRepository: ProjectRepository,
//    private val tokenProvider: TokenProvider,
    private val finalizedLectureRepository: FinalizedLectureRepository
) : ViewModel() {

    private val _projects = MutableStateFlow<List<Project>>(emptyList())
    val projects: StateFlow<List<Project>> = _projects
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error
    private val _selectedProject = MutableStateFlow<Project?>(null)
    val selectedProject: StateFlow<Project?> = _selectedProject
    private val _videoUrl = MutableStateFlow<String?>(null)
    val videoUrl: StateFlow<String?> = _videoUrl
    private val _lectures = MutableStateFlow<List<FinalizedLecture>>(emptyList())
    val lectures: StateFlow<List<FinalizedLecture>> = _lectures


//    fun fetchProjects() {
//        viewModelScope.launch {
//            _isLoading.value = true
//            try {
//                val response = projectRepository.getProjects()
//                if (response.isSuccessful) {
//                    response.body()?.let {
//                        _projects.value = it
//                    }
//                } else {
//                    _error.value = "Error: ${response.code()}"
//                }
//            } catch (e: Exception) {
//                _error.value = e.message
//            } finally {
//                _isLoading.value = false
//            }
//        }
//    }

    fun fetchFinalizedLectures(projectId: String) {
        viewModelScope.launch {
            try {
                val response = finalizedLectureRepository.getFinalizedLectures(projectId)

                if (response.isSuccessful) {
                    val list = response.body()
                    _lectures.value = list ?: emptyList()
                    _videoUrl.value = list?.firstOrNull { !it.videoFileUrl.isNullOrBlank() }?.videoFileUrl
                } else {
                    val errorText = response.errorBody()?.string()
                    _videoUrl.value = null
                }
            } catch (e: Exception) {
                _videoUrl.value = null
            }
        }
    }


    fun fetchProjects() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = projectRepository.getProjects()
                if (response.isSuccessful) {
                    response.body()?.let { projectResponse ->
                        _projects.value = projectResponse.content
                    }
                } else {
                    _error.value = "Error: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }


    fun fetchProjectById(projectId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = projectRepository.getProjects() // vẫn trả về list
                if (response.isSuccessful) {
                    val list = response.body()?.content
                    _selectedProject.value = list?.find { it.id == projectId }
                } else {
                    _error.value = "Error: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

}
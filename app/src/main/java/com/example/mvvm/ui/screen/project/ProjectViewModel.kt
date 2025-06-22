package com.example.mvvm.ui.screen.project

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvm.models.Project
import com.example.mvvm.repositories.apis.project.ProjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProjectViewModel @Inject constructor(
    private val projectRepository: ProjectRepository
) : ViewModel() {

    private val _projects = MutableStateFlow<List<Project>>(emptyList())
    val projects: StateFlow<List<Project>> = _projects
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error
    private val _selectedProject = MutableStateFlow<Project?>(null)
    val selectedProject: StateFlow<Project?> = _selectedProject

    fun fetchProjects() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = projectRepository.getProjects()
                if (response.isSuccessful) {
                    response.body()?.let {
                        _projects.value = it
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
                    val list = response.body()
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
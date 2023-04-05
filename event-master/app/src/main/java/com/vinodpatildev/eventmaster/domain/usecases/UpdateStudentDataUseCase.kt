package com.vinodpatildev.eventmaster.domain.usecases

import com.vinodpatildev.eventmaster.data.model.ApiResponse
import com.vinodpatildev.eventmaster.data.model.Student
import com.vinodpatildev.eventmaster.data.util.Resource
import com.vinodpatildev.eventmaster.domain.repository.Repository

class UpdateStudentDataUseCase(private val repository: Repository)  {
    suspend fun execute(cookies:String, studentId:String, name:String, registration_no:String, dob:String, mobile_no:String, year:String, department:String, division:String, passing_year:String): Resource<ApiResponse<Student>> {
        return repository.updateStudentData(cookies, studentId, name, registration_no, dob, mobile_no, year, department, division, passing_year)
    }
}
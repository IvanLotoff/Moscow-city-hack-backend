package ivan.projects.hakatonbackend.repository

import ivan.projects.hakatonbackend.domain.User

interface UserRepository {
    fun findUserById(id : String) : User?
}
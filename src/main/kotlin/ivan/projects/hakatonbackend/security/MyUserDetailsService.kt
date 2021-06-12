package ivan.projects.hakatonbackend.security

import ivan.projects.hakatonbackend.repository.UserRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class MyUserDetailsService(private val userRepository: UserRepository) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findUserById(username) ?: throw UsernameNotFoundException("User not found")
        return User.withUsername(username)
            .password(user.password)
            .authorities(user.authority)
            .accountExpired(false)
            .accountLocked(false)
            .disabled(false)
            .build()
    }
}
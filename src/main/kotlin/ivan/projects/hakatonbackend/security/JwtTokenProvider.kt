package ivan.projects.hakatonbackend.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import ivan.projects.hakatonbackend.exception.CustomException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import java.util.*
import javax.annotation.PostConstruct
import javax.servlet.http.HttpServletRequest

@Component
class JwtTokenProvider(
    /**
     * здесь мы инжектим UserDetails
     */
    private val myUserDetailsService: MyUserDetailsService
) {
    private var secretKey = "jwtsecret";
    // 1h
    private val validityInMilliseconds = 3600000;

    fun createToken(username: String, authority : String): String? {
        val claims = Jwts.claims().setSubject(username)
        claims.put("auth", SimpleGrantedAuthority(authority))
        val now = Date()
        val validity = Date(now.time + validityInMilliseconds)

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact()
    }
    fun getAuthentication(token : String): Authentication {
        val userDetails = myUserDetailsService.loadUserByUsername(getUsername(token))
        return UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
    }
    fun getUsername(token : String) : String{
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).body.subject
    }
    fun resolverToken(request : HttpServletRequest) : String? {
        val bearerToken = request.getHeader("Authorization")
        if(bearerToken != null && bearerToken.startsWith("Authorization"))
            return bearerToken.substring(7)
        return null
    }
    fun validateToken(token : String) : Boolean{
        try{
            Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
            // не упало => токен валиден
            return true
        } catch(ex : Exception){
            throw CustomException()
        }
    }
}
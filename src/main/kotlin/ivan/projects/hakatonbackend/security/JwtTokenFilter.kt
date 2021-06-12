package ivan.projects.hakatonbackend.security

import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Поскольку  у нас происходит запрос к базе данных
 * мы расширяем OncePerRequestFilter
 * Мы не можем применить аннотацию @Component,
 * поскольку данный фильтр должен быть перед всеми остальными,
 * для этого используется другой механизм
 */
class JwtTokenFilter(
    /**
     * Cюда нужно заинжектить jwtTokenProvider
     */
    private val jwtTokenProvider: JwtTokenProvider
) : OncePerRequestFilter() {
    override fun doFilterInternal(httpServletRequest:  HttpServletRequest,
                                  httpServletResponse: HttpServletResponse,
                                  chain: FilterChain) {
        /**
         * Делегируем разрешение токена токен провайдеру
         * val token = jwtTokenProvider.resolverToken(httpServletRequest)
         */
        val token = jwtTokenProvider.resolverToken(httpServletRequest)
        try{
            val auth = jwtTokenProvider.getAuthentication(token!!)
            SecurityContextHolder.getContext().authentication = auth
        }
        catch(Ex : Exception){
            SecurityContextHolder.clearContext()
            httpServletResponse.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid token")
            return;
        }
        // =========================
        /**
         * try{
         *     проверяем токен на null и валидируем его
         *     val auth : Authentication = jwtTokenProvider.getAuthentication(token)
         *     SecurityContextHolder.context.authentication = auth
         *     }
         * catch(CustomException ex) {
         *     очищаем securityContext (securityContextHolder.clearContext())
         *     отправляем ошибку в сервлет респонс
         *     httpServletResponse.sendError(ex.httpStatus.value, ex.message)
         *
         */
        chain.doFilter(httpServletRequest, httpServletResponse)
    }
}

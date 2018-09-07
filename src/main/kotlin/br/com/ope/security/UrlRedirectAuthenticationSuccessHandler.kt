package br.com.ope.security

import br.com.ope.model.Aluno
import org.springframework.security.core.Authentication
import org.springframework.security.web.DefaultRedirectStrategy
import org.springframework.security.web.WebAttributes
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import java.io.IOException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class UrlRedirectAuthenticationSuccessHandler(private var redirectStrategy : DefaultRedirectStrategy = DefaultRedirectStrategy()) : AuthenticationSuccessHandler {
    @Throws(IOException::class)
    override fun onAuthenticationSuccess(request: HttpServletRequest,
                                         response: HttpServletResponse, authentication: Authentication) {

        handle(request, response, authentication)
        clearAuthenticationAttributes(request)
    }

    @Throws(IOException::class)
    private fun handle(request: HttpServletRequest,
                         response: HttpServletResponse, authentication: Authentication) {

        val targetUrl = determineTargetUrl(authentication)

        if (response.isCommitted) {
            println("A resposta nao pode ser redirecionada para: $targetUrl")
            return
        }

        redirectStrategy.sendRedirect(request, response, targetUrl)
    }

    private fun determineTargetUrl(authentication: Authentication): String {
        return if (authentication.principal is Aluno) {
            "/painel/aluno"
        } else {
            return "/"
        }
    }

    private fun clearAuthenticationAttributes(request: HttpServletRequest) {
        val session = request.getSession(false) ?: return
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION)
    }

}
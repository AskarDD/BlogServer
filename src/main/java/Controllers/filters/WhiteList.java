package Controllers.filters
        ;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class WhiteList implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpSession httpSession = ((HttpServletRequest)request).getSession();

        if (httpSession != null){
            if (httpSession.getAttribute("isOuts") != null){
                chain.doFilter(request,response);
            }else{
                ((HttpServletResponse)response).sendRedirect("/server/login");
            }
        }else{
            ((HttpServletResponse)response).sendRedirect("/server/login");
        }
    }

    @Override
    public void destroy() {

    }
}

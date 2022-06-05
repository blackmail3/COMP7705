package hku.cs.security;

import cn.hutool.core.util.StrUtil;
import hku.cs.entity.User;
import hku.cs.service.UserService;
import hku.cs.util.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends BasicAuthenticationFilter {
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    UserService userService;
    @Autowired
    UserDetailServiceImpl userDetailService;
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager){
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String jwt = request.getHeader(jwtUtils.getHeader());
        if (StrUtil.isBlankOrUndefined(jwt)) {
            chain.doFilter(request, response);
            return;
        }

        Claims claim = jwtUtils.getClaimByToken(jwt);
        if (claim == null) {
            throw new JwtException("token  Exception");
        }
        if (jwtUtils.isTokenExpired(claim)) {
            throw new JwtException("token Expired");
        }

        String username = claim.getSubject();

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, null, null);
        SecurityContextHolder.getContext().setAuthentication(token);
        chain.doFilter(request, response);
        // Obtain information such as user permissions

//        User user = userService.getByUsername(username);
//        UsernamePasswordAuthenticationToken token
//                = new UsernamePasswordAuthenticationToken(username, null, userDetailService.getUserAuthority(user.getId()));
//
//        SecurityContextHolder.getContext().setAuthentication(token);
//
//        chain.doFilter(request, response);
    }
}

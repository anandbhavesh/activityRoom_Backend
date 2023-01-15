package com.activityWindow_Backend.activityWindow.Config;


import com.activityWindow_Backend.activityWindow.JwtHelper.JwtUtil;
import com.activityWindow_Backend.activityWindow.Services.UserDetailsServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    @Autowired
    private JwtUtil jwtutill;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {


        String TokenHeader = request.getHeader("Authorization");
        String userName = null;
        String jwtToken = null;


        if(TokenHeader!=null && TokenHeader.startsWith("Bearer "))

        {

            jwtToken = TokenHeader.substring(7);

            try{
                userName = jwtutill.extractUsername(jwtToken);

            }catch(Exception e){

                e.printStackTrace();
            }


            UserDetails userDetails = userDetailsService.loadUserByUsername(userName);

            if(jwtutill.validateToken(jwtToken,userDetails)){

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
                        null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);


            }

        }


        filterChain.doFilter(request, response);

    }
}
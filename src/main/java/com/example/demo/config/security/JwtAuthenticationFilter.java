package com.example.demo.config.security;

import com.example.demo.user.repository.BlacklistedTokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * JWT 인증 필터
 *
 * OncePerRequestFilter:
 *   하나의 요청에 딱 한 번만 실행되는 필터 (중복 실행 방지)
 *
 * 동작 순서:
 *   1. 요청 헤더에서 "Authorization: Bearer {token}" 추출
 *   2. 토큰 유효성 검증 (JwtTokenProvider.validateToken)
 *   3. 유효하면 토큰에서 loginId 추출
 *   4. SecurityContext에 인증 정보 저장
 *   5. 다음 필터로 요청 전달
 *
 * SecurityContext에 저장된 인증 정보는 컨트롤러에서
 * @AuthenticationPrincipal String loginId 로 꺼낼 수 있음
 */
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    private final JwtTokenProvider jwtTokenProvider;
    private final BlacklistedTokenRepository blacklistedTokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        //  헤더에서 토큰 추출
        String token = resolveToken(request);

        //   토큰이 존재하고 유효하면 인증 처리
        if (token != null && jwtTokenProvider.validateToken(token)) {

            //블랙리스트 체크
            //로그아웃된 토큰이면 인증처리 안하고 그냥 통과
            if (blacklistedTokenRepository.existsByToken(token)) {
                filterChain.doFilter(request , response);
                return;
            }

            String loginId = jwtTokenProvider.getLoginIdFromToken(token);

           /* *
             * Step 4: SecurityContext에 인증 정보 저장
             * UsernamePasswordAuthenticationToken(principal, credentials, authorities)
             *   principal:   loginId (나중에 @AuthenticationPrincipal로 꺼냄)
             *   credentials: null (토큰 방식이라 비밀번호 불필요)
             *   authorities: 권한 목록*/
          UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            loginId,
                            null,
                            List.of(new SimpleGrantedAuthority("ROLE_USER"))
                    );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // Step 5: 다음 필터로 전달 (토큰 없어도 일단 통과, SecurityConfig에서 최종 판단)
        filterChain.doFilter(request, response);
    }

    /**
     * Authorization 헤더에서 토큰 추출
     * "Bearer eyJhbGci..." → "eyJhbGci..."
     * Bearer 뒤 공백 포함 7글자를 잘라냄
     */
    private String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;  // Authorization 헤더 없거나 형식 다르면 null



    }
}
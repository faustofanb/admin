package io.github.faustofan.admin.shared.web.filter;

import io.github.faustofan.admin.shared.common.context.AppContext;
import io.github.faustofan.admin.shared.common.context.AppContextHolder;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.SimpleIdGenerator;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * Filter that generates a trace ID for each incoming HTTP request if one is not
 * already present.
 */
@Component
public class TraceIdGenFilter extends OncePerRequestFilter {
    /**
     * Filter method that ensures an AppContext with a trace ID is present for each
     * request.
     *
     * @param request     the HTTP request
     * @param response    the HTTP response
     * @param filterChain the filter chain
     * @throws ServletException in case of a servlet error
     * @throws IOException      in case of an I/O error
     */
    @Override
    protected void doFilterInternal(
            @Nonnull HttpServletRequest request,
            @Nonnull HttpServletResponse response,
            @Nonnull FilterChain filterChain) throws ServletException, IOException {

        try {
            AppContext ctx = AppContextHolder.getContext();
            if (ctx == null) {
                ctx = new AppContext(
                        UUID.randomUUID(),
                        new SimpleIdGenerator().generateId().toString(),
                        null,
                        null,
                        null,
                        null,
                        null,
                        false);
            }
            AppContextHolder.setContext(ctx);
            filterChain.doFilter(request, response);
        } finally {
            AppContextHolder.clearContext();
        }
    }
}

package com.socialmedia.social_media_frontend.util;

import org.springframework.ui.Model;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public final class PaginationUtils {

    private static final int DEFAULT_PAGE_SIZE = 10;

    private PaginationUtils() {
    }

    public static <T> String renderPaginatedResult(
            List<T> sourceData,
            int page,
            int size,
            String pagePath,
            String resultView,
            Model model,
            Map<String, ?> queryParams) {

        List<T> safeData = sourceData == null ? Collections.emptyList() : sourceData;
        int safeSize = size > 0 ? size : DEFAULT_PAGE_SIZE;
        int totalItems = safeData.size();
        int totalPages = Math.max(1, (int) Math.ceil((double) totalItems / safeSize));
        int currentPage = Math.max(0, Math.min(page, totalPages - 1));

        int fromIndex = Math.min(currentPage * safeSize, totalItems);
        int toIndex = Math.min(fromIndex + safeSize, totalItems);

        boolean hasPrevious = currentPage > 0;
        boolean hasNext = currentPage < totalPages - 1;

        model.addAttribute("data", safeData.subList(fromIndex, toIndex));
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("pageSize", safeSize);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("hasPrevious", hasPrevious);
        model.addAttribute("hasNext", hasNext);
        model.addAttribute("previousPageUrl",
                hasPrevious ? buildPageUrl(pagePath, currentPage - 1, safeSize, queryParams) : "#");
        model.addAttribute("nextPageUrl",
                hasNext ? buildPageUrl(pagePath, currentPage + 1, safeSize, queryParams) : "#");

        return resultView;
    }

    private static String buildPageUrl(String pagePath, int targetPage, int size, Map<String, ?> queryParams) {
        StringBuilder url = new StringBuilder(pagePath)
                .append("?page=")
                .append(targetPage)
                .append("&size=")
                .append(size);

        if (queryParams != null) {
            for (Map.Entry<String, ?> entry : queryParams.entrySet()) {
                Object value = entry.getValue();
                if (value == null) {
                    continue;
                }

                String stringValue = String.valueOf(value).trim();
                if (stringValue.isEmpty()) {
                    continue;
                }

                url.append("&")
                        .append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8))
                        .append("=")
                        .append(URLEncoder.encode(stringValue, StandardCharsets.UTF_8));
            }
        }

        return url.toString();
    }
}

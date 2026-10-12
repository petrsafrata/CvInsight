package cz.jpmad.cvinsight.service.info;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Provides information about the application – version, release notes, ToS, privacy policy.
 * Markdown files are rendered to HTML and optionally cached.
 */
@Slf4j
@Service
public class InfoService {

    private final Resource versionResource;
    private final Resource releaseNotesResource;
    private final Resource tosResource;
    private final Resource privacyPolicyResource;
    private final long cacheTtlMs;

    private final Parser parser = Parser.builder().build();
    private final HtmlRenderer renderer = HtmlRenderer.builder().build();

    private final ConcurrentMap<String, CacheEntry> cache = new ConcurrentHashMap<>();

    public InfoService(
            @Value("file:VERSION") final Resource versionResource,
            @Value("classpath:RELEASE-NOTES.md") final Resource releaseNotesResource,
            @Value("classpath:TERMS_OF_USE.md") final Resource tosResource,
            @Value("classpath:PRIVACY_POLICY.md") final Resource privacyPolicyResource,
            @Value("${info.cacheTtlMs:0}") final long cacheTtlMs
    ) {
        this.versionResource = versionResource;
        this.releaseNotesResource = releaseNotesResource;
        this.tosResource = tosResource;
        this.privacyPolicyResource = privacyPolicyResource;
        this.cacheTtlMs = cacheTtlMs;
    }

    /**
     * Retrieves the project version from the VERSION file.
     *
     * @return the project version as a string
     * @throws IOException if an I/O error occurs while reading the VERSION file
     */
    public String getProjectVersion() throws IOException {
        return readResource(versionResource).trim();
    }

    /**
     * Retrieves the release notes from the RELEASE-NOTES.md file, rendering it to HTML.
     * The result is cached based on the configured cache TTL.
     *
     * @return the rendered release notes as HTML
     * @throws IOException if an I/O error occurs while reading the RELEASE-NOTES.md file
     */
    public String getReleaseNotes() throws IOException {
        return getCachedOrRender("releaseNotes", releaseNotesResource);
    }

    /**
     * Retrieves the Terms of Service from the TERMS_OF_USE.md file, rendering it to HTML.
     * The result is cached based on the configured cache TTL.
     *
     * @return the rendered Terms of Service as HTML
     * @throws IOException if an I/O error occurs while reading the TERMS_OF_USE.md file
     */
    public String getTermsOfService() throws IOException {
        return getCachedOrRender("tos", tosResource);
    }

    /**
     * Retrieves the Privacy Policy from the PRIVACY_POLICY.md file, rendering it to HTML.
     * The result is cached based on the configured cache TTL.
     *
     * @return the rendered Privacy Policy as HTML
     * @throws IOException if an I/O error occurs while reading the PRIVACY_POLICY.md file
     */
    public String getPrivacyPolicy() throws IOException {
        return getCachedOrRender("privacyPolicy", privacyPolicyResource);
    }

    /**
     * Retrieves the cached HTML for a given key or renders the Markdown resource to HTML if not cached or expired.
     *
     * @param key      the cache key
     * @param resource the Markdown resource to render
     * @return the rendered HTML
     * @throws IOException if an I/O error occurs while reading the resource
     */
    private String getCachedOrRender(final String key, final Resource resource) throws IOException {
        if (cacheTtlMs <= 0) {
            return renderMarkdown(resource);
        }

        final long now = System.currentTimeMillis();
        final CacheEntry entry = cache.get(key);

        if (entry != null && (now - entry.createdAtMs()) < cacheTtlMs) {
            return entry.html();
        }

        final String html = renderMarkdown(resource);
        cache.put(key, new CacheEntry(html, now));
        log.info("Cached '{}' at {}", key, now);
        return html;
    }

    /**
     * Renders the content of a Markdown resource to HTML.
     *
     * @param resource the Markdown resource to render
     * @return the rendered HTML
     * @throws IOException if an I/O error occurs while reading the resource
     */
    private String renderMarkdown(final Resource resource) throws IOException {
        final String markdown = readResource(resource);
        return renderer.render(parser.parse(markdown));
    }

    /**
     * Reads the content of a resource as a UTF-8 string.
     *
     * @param resource the resource to read
     * @return the content of the resource as a string
     * @throws IOException if an I/O error occurs
     */
    private String readResource(final Resource resource) throws IOException {
        if (resource == null || !resource.exists()) {
            throw new NoSuchElementException("Resource not found on classpath");
        }
        try (var in = resource.getInputStream()) {
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("Failed to read resource: {}", resource.getFilename(), e);
            throw new UncheckedIOException("Failed to read resource: " + resource.getFilename(), e);
        }
    }

    /**
     * Represents a cached entry with the rendered HTML and the timestamp of when it was created.
     */
    private record CacheEntry(String html, long createdAtMs) {
    }
}

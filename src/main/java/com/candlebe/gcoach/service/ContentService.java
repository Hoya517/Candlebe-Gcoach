package com.candlebe.gcoach.service;

import com.candlebe.gcoach.dto.ContentUploadDTO;
import com.candlebe.gcoach.entity.Content;
import com.candlebe.gcoach.exception.StorageException;
import com.candlebe.gcoach.repository.ContentRepository;
import com.candlebe.gcoach.storage.StorageProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContentService {

    private final ContentRepository contentRepository;
    private final Path rootLocation;

    @Autowired
    public ContentService(ContentRepository contentRepository, StorageProperties properties) {
        this.contentRepository = contentRepository;
        this.rootLocation = Paths.get(properties.getLocation());
    }

    // 삭제
    @Transactional
    public void deleteContent(Long cid) {
        contentRepository.deleteContent(cid);
    }

    // 콘텐츠 파일 저장
    public Content save(MultipartFile file, MultipartFile img, ContentUploadDTO dto) {
        try {
            // file == mp3
            // img 추가
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file.");
            }
            if (img.isEmpty()) {
                throw new StorageException("Failed to store empty img.");
            }
            Path filePath = this.rootLocation.resolve(
                    Paths.get(file.getOriginalFilename()))
                    .normalize().toAbsolutePath();
            Path imgPath = this.rootLocation.resolve(
                    Paths.get(img.getOriginalFilename()))
                    .normalize().toAbsolutePath();
            if (!filePath.getParent().equals(this.rootLocation.toAbsolutePath())) {
                // This is a security check
                throw new StorageException(
                        "Cannot store file outside current directory.");
            }
            if (!imgPath.getParent().equals(this.rootLocation.toAbsolutePath())) {
                // This is a security check
                throw new StorageException(
                        "Cannot store img outside current directory.");
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, filePath,
                        StandardCopyOption.REPLACE_EXISTING);
            }
            try (InputStream inputStream = img.getInputStream()) {
                Files.copy(inputStream, imgPath,
                        StandardCopyOption.REPLACE_EXISTING);
            }

            Content content = new Content(
                    dto.getCategory1(),
                    dto.getCategory2(),
                    dto.getCategory3(),
                    dto.getTitle(),
                    dto.getContent(),
                    filePath.toString(),
                    file.getOriginalFilename(),
                    imgPath.toString(),
                    img.getOriginalFilename()
            );
            return contentRepository.save(content);
        }
        catch (IOException e) {
            throw new StorageException("Failed to store file.", e);
        }
    }

    //콘텐츠 전체 조회
    public List<Content> findContents() {
        return contentRepository.findAll();
    }

    // 받아온 검색어로 콘텐츠 제목 검색 후 리턴
    public List<Content> findBySearch(String search) {
        return contentRepository.findBySearch(search);
    }

    /* 콘텐츠 조회 */
    // 해당 카테고리로 콘텐츠 조회 후 카테고리에 맞는 콘텐츠 리스트 리턴
    public List<Content> findContentsByCategory(String category) {
        return contentRepository.findByCategory(category);
    }

    public List<Content> findContentsByCategory(String category1, String category2) {
        return contentRepository.findByCategory(category1, category2);
    }

    public List<Content> findContentsByCategory(String category1, String category2, String category3) {
        return contentRepository.findByCategory(category1, category2, category3);
    }

    // 받아온 관심 분야와 감정을 통해 추천 콘텐츠 조회 후 그에 맞는 리스트 리턴
    public List<Content> findContentsForUser(String interest, String emotion) {
        return contentRepository.findByUsers(interest, emotion);
    }

    public Optional<Content> findOne(Long id) {
        return contentRepository.findById(id);
    }

    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    public void init() {
        try {
            Files.createDirectories(rootLocation);
        }
        catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }
}

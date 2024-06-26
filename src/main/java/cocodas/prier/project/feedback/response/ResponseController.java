package cocodas.prier.project.feedback.response;

import cocodas.prier.project.feedback.response.dto.ResponseDetailDto;
import cocodas.prier.project.feedback.response.dto.UserResponseProjectDto;
import cocodas.prier.project.feedback.response.dto.ResponseDto;
import cocodas.prier.project.feedback.response.dto.ResponseRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects")
public class ResponseController {

    private final ResponseService responseService;

    private static String getToken(String auth) {
        if (auth == null || !auth.startsWith("Bearer ")) {
            throw new RuntimeException("JWT Token is missing");
        }

        return auth.substring(7);
    }

    // 프로젝트 응답 상세보기
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{projectId}/responses")
    public ResponseDetailDto viewResponseDetail(@PathVariable(name = "projectId") Long projectId,
                                                Authentication authentication) {
        Long userId = Long.valueOf(authentication.getName());
        return responseService.viewResponseDetail(projectId, userId);
    }

    // 응답 등록하기
    @PostMapping("/{projectId}/responses")
    public ResponseEntity<List<ResponseDto>> createResponses(@PathVariable(name = "projectId") Long projectId,
                                                             @RequestBody List<ResponseRequestDto> responsesDto,
                                                             Authentication authentication) {
        if (responsesDto.isEmpty()) {
            throw new IllegalArgumentException("Responses cannot be empty");
        }
        Long userId = Long.valueOf(authentication.getName());
        List<ResponseDto> createdResponses = responseService.createResponses(userId, responsesDto);
        return ResponseEntity.ok(createdResponses);
    }

    // 자신의 피드백 삭제
    @DeleteMapping("/{projectId}/responses")
    public ResponseEntity<String> deleteResponsesByUserAndProject(@PathVariable(name = "projectId") Long projectId,
                                                                  Authentication authentication) {
        Long userId = Long.valueOf(authentication.getName());
        responseService.deleteResponses(projectId, userId);
        return ResponseEntity.ok("Responses for project ID " + projectId + " by user ID " + userId + " have been deleted.");
    }

    // 자신이 응답을 남긴 프로젝트 목록 조회
    @GetMapping("/my-feedbacks")
    public ResponseEntity<List<UserResponseProjectDto>> getProjectsByUser(Authentication authentication) {
        Long userId = Long.valueOf(authentication.getName());
        List<Long> projectIds = responseService.getProjectsByUser(userId);
        List<UserResponseProjectDto> projectDtos = projectIds.stream()
                .map(projectId -> UserResponseProjectDto.builder().projectId(projectId).build())
                .collect(Collectors.toList());
        return ResponseEntity.ok(projectDtos);
    }

    // 질문별 응답 조회
    @GetMapping("/{projectId}/{questionId}/responses")
    public ResponseEntity<List<ResponseDto>> getResponsesByQuestion(@PathVariable Long projectId,
                                                                    @PathVariable Long questionId) {
        List<ResponseDto> responses = responseService.getResponsesByQuestion(questionId);
        return ResponseEntity.ok(responses);
    }





}

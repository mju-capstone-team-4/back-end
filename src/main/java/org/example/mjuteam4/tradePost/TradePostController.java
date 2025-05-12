package org.example.mjuteam4.tradePost;

import lombok.RequiredArgsConstructor;
import org.example.mjuteam4.global.uitl.JwtUtil;
import org.example.mjuteam4.tradePost.dto.request.TradePostRequest;
import org.example.mjuteam4.tradePost.dto.response.TradePostResponse;
import org.example.mjuteam4.tradePost.entity.TradePost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/trade")
public class TradePostController {
    private final TradePostService tradePostService;
    private final JwtUtil jwtUtil;

    @GetMapping("/my")
    public ResponseEntity<Page<TradePostResponse>> myTradePostList(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Long id = jwtUtil.getLoginMember().getId();

        Pageable pageable = PageRequest.of(page, size);
        Page<TradePost> memberTradPost = tradePostService.getMyTradPost(id, pageable);
        Page<TradePostResponse> response = memberTradPost.map(TradePostResponse::create);
        return ResponseEntity.ok().body(response);
    }
    @PostMapping("/create")
    public ResponseEntity<TradePostResponse> tradePostCreate(@ModelAttribute TradePostRequest tradePostRequest) {
        Long memberId = jwtUtil.getLoginMember().getId();
        TradePost tradePost = tradePostService.createTradePost(memberId, tradePostRequest);
        TradePostResponse tradePostResponse = TradePostResponse.create(tradePost);
        return ResponseEntity.ok().body(tradePostResponse);
    }

    @GetMapping("/all")
    public ResponseEntity<Page<TradePostResponse>> tradePostList(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        Page<TradePost> tradePosts = tradePostService.tradePostList(pageable);
        Page<TradePostResponse> response = tradePosts.map(TradePostResponse::create);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{trade-post-id}")
    public ResponseEntity<TradePostResponse> tradePostDetail(@PathVariable(value = "trade-post-id") Long questionId) {
        TradePost tradePost = tradePostService.tradePostDetail(questionId);
        TradePostResponse tradePostResponse = TradePostResponse.create(tradePost);
        return ResponseEntity.ok().body(tradePostResponse);
    }

    @PutMapping("/{trade-post-id}")
    public ResponseEntity<TradePostResponse> tradePostModify(
            @PathVariable(value = "trade-post-id") Long tradePostId,
            @ModelAttribute TradePostRequest tradePostRequest
    ) {
        Long memberId = jwtUtil.getLoginMember().getId();
        TradePost tradePost = tradePostService.modifyTradePost(memberId, tradePostId, tradePostRequest);
        TradePostResponse tradePostResponse = TradePostResponse.create(tradePost);
        return ResponseEntity.ok().body(tradePostResponse);
    }

    @DeleteMapping("/{trade-post-id}")
    public ResponseEntity<String> tradePostDelete(
            @PathVariable(value = "trade-post-id") Long tradePostId)
    {
        Long memberId = jwtUtil.getLoginMember().getId();
        tradePostService.deleteTradePost(memberId, tradePostId);
        return ResponseEntity.ok().body("Deleted Success");
    }

}

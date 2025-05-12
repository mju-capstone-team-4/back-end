package org.example.mjuteam4.tradePost;

import lombok.RequiredArgsConstructor;

import org.example.mjuteam4.global.uitl.JwtUtil;
import org.example.mjuteam4.mypage.entity.Member;
import org.example.mjuteam4.storage.StorageService;
import org.example.mjuteam4.tradePost.dto.request.TradePostRequest;
import org.example.mjuteam4.tradePost.entity.TradePost;
import org.example.mjuteam4.tradePost.exception.TradePostNotFound;
import org.example.mjuteam4.tradePostImage.TradePostImageService;
import org.example.mjuteam4.tradePostImage.entity.TradePostImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TradePostService {
    private final TradePostRepository tradePostRepository;
    private final TradePostImageService tradePostImageService;
    private final StorageService storageService;
    private final JwtUtil jwtUtil;

    public TradePost createTradePost(Long memberId, TradePostRequest tradePostRequest) {
        TradePost tradePost = TradePost.create(tradePostRequest);
        TradePostImage tradePostImage = tradePostImageService.createTradePostImage(tradePostRequest.getImage());
        tradePost.addTradePostImage(tradePostImage);
        Member member = jwtUtil.getLoginMember();
        member.addTradePost(tradePost);
        return tradePostRepository.save(tradePost);
    }

    public Page<TradePost> tradePostList(Pageable pageable) {
        return tradePostRepository.findAll(pageable);
    }

    public TradePost tradePostDetail(Long tradePostId) {
        return tradePostRepository.findById(tradePostId).orElseThrow(TradePostNotFound::new);
    }

    public TradePost modifyTradePost(Long memberId, Long tradePostId, TradePostRequest tradePostRequest) {
        // 수정 대상 질문 조회 후 제목, 내용 수정
        TradePost tradePost = checkAccessAndReturnTradePost(memberId, tradePostId);
        TradePost modifiedTradePost = tradePost.modifyTradePost(tradePostRequest);

        // 이미지 수정
        // 이미지 변경 요청이 있다면, 기존 s3이미지 삭제하고 새로운 이미지 업로드
        if(tradePostRequest.getImage() != null){

            String originalImageUrl = modifiedTradePost.getTradePostImage().getImageUrl();
            tradePostImageService.deleteImageService(originalImageUrl);
            modifiedTradePost.setTradePostImage(null);
            tradePostRepository.flush();

            MultipartFile image = tradePostRequest.getImage();
            TradePostImage tradePostImage = tradePostImageService.createTradePostImage(image);
            modifiedTradePost.addTradePostImage(tradePostImage);
        }

        return modifiedTradePost;
    }

    public void deleteTradePost(Long memberId, Long tradePostId) {
        // 1. 권한 체크
        checkAccessAndReturnTradePost(memberId, tradePostId);

        // 2. s3 버킷에 있는 이미지 우선 삭제
        TradePost tradePost = tradePostRepository.findById(tradePostId).orElseThrow(TradePostNotFound::new);
        String imageUrl = tradePost.getTradePostImage().getImageUrl();
        tradePostImageService.deleteImageService(imageUrl);

        // 2. db row 삭제
        tradePostRepository.deleteById(tradePostId);
    }

    public TradePost checkAccessAndReturnTradePost(Long memberId, Long tradePostId){
        TradePost tradePost = tradePostRepository.findById(tradePostId).orElseThrow(TradePostNotFound::new);
        if(tradePost.getMember().getId() != memberId){
            throw new TradePostNotFound();
        }
        return tradePost;
    }

    public Page<TradePost> getMyTradPost(Long memberId) {
        return tradePostRepository.findByMemberId(memberId);
    }

}

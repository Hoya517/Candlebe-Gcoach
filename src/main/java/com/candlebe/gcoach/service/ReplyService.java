package com.candlebe.gcoach.service;

import com.candlebe.gcoach.dto.ReplyDTO;
import com.candlebe.gcoach.entity.Content;
import com.candlebe.gcoach.entity.Member;
import com.candlebe.gcoach.entity.Reply;

import java.util.List;

public interface ReplyService {

    Long register(ReplyDTO replyDTO);

    List<ReplyDTO> getListOfReply(Long cid);

    default Reply dtoToEntity(ReplyDTO replyDTO) {
        return Reply.builder()
                .content(Content.builder().cid(replyDTO.getCid()).build())
                .text(replyDTO.getText())
                .member(Member.builder().mid(replyDTO.getMid()).build())
                .build();
    }

    default ReplyDTO entityToDto(Reply reply) {
        return ReplyDTO.builder()
                .rid(reply.getRid())
                .mid(reply.getMember().getMid())
                .nickname(reply.getMember().getNickname())
                .cid(reply.getContent().getCid())
                .text(reply.getText())
                .regDate(reply.getRegDate())
                .modDate(reply.getModDate())
                .build();
    }
}

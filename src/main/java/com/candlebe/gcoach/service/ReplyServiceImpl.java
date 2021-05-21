package com.candlebe.gcoach.service;

import com.candlebe.gcoach.dto.ReplyDTO;
import com.candlebe.gcoach.entity.Content;
import com.candlebe.gcoach.entity.Reply;
import com.candlebe.gcoach.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService {

    private final ReplyRepository replyRepository;

    @Override
    public Long register(ReplyDTO replyDTO) {
        Reply reply = dtoToEntity(replyDTO);
        replyRepository.save(reply);
        return reply.getRid();
    }

    @Override
    public List<ReplyDTO> getListOfReply(Long cid) {
        Content content = Content.builder().cid(cid).build();
        List<Reply> result = replyRepository.findByContent(content);
        return result.stream().map(this::entityToDto).collect(Collectors.toList());
    }
}

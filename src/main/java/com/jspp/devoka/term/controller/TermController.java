package com.jspp.devoka.term.controller;

import com.jspp.devoka.term.dto.CreateTermRequest;
import com.jspp.devoka.term.dto.TermResponse;
import com.jspp.devoka.term.dto.UpdateTermRequest;
import com.jspp.devoka.term.service.TermService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/term")
public class TermController {

    private final TermService termService;

    // 용어 생성
    @PostMapping
    public ResponseEntity<TermResponse> createTerm(@RequestBody CreateTermRequest termRequest){

        return new ResponseEntity<>(termService.createTerm(termRequest), HttpStatus.OK);
    }

    // 용어 리스트 조회
    @GetMapping
    public ResponseEntity<Page<TermResponse>> getTermList(@RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "10") int size){

        return new ResponseEntity<>(termService.getTermList(page, size), HttpStatus.OK); // 200 OK 응답
    }

    // 용어 검색
    @GetMapping("/search")
    public ResponseEntity<Page<TermResponse>> searchTerm(@RequestParam(name = "keyword") String keyword,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size){

        return new ResponseEntity<>(termService.searchTerm(keyword, page, size), HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<TermResponse> updateTerm(@RequestBody UpdateTermRequest termRequest){

        return new ResponseEntity<>(termService.updateTerm(termRequest), HttpStatus.OK);
    }



}

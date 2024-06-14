package com.ksy1028.ksylunchproject.service;

import com.ksy1028.ksylunchproject.domain.Lunch;
import com.ksy1028.ksylunchproject.dto.LunchDTO;
import com.ksy1028.ksylunchproject.dto.PageRequestDTO;
import com.ksy1028.ksylunchproject.dto.PageResponseDTO;
import com.ksy1028.ksylunchproject.repository.LunchRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
// 모델 맵퍼 도구 설치.
public class LunchServiceImpl implements LunchService {
    // 의존성 주입
    private final LunchRepository lunchRepository;
    private final ModelMapper modelMapper;


    @Override
    public Long register(LunchDTO lunchDTO) {
        // 화면에서 작성한 게시글 내용이 DTO 있고, -> VO 대신 entity 클래스 이용중.
        Lunch lunch = modelMapper.map(lunchDTO, Lunch.class);
        Long mno = lunchRepository.save(lunch).getMno();
        return mno;
    }

    @Override
    public LunchDTO read(Long mno) {
        // 1차 영속성 컨텍스트에서 조회한 내용을 가져오기.
        Optional<Lunch> result = lunchRepository.findById(mno);
        // 만약 있다면, 엔티티 타입으로 담기.(VO 같은 개념)
        Lunch lunch = result.orElseThrow();
        // 엔티티 -> DTO 변환.
        LunchDTO lunchDTO = modelMapper.map(lunch, LunchDTO.class);

        return lunchDTO;
    }

    @Override
    public void update(LunchDTO lunchDTO) {
        Optional<Lunch> result = lunchRepository.findById(lunchDTO.getMno());
        Lunch lunch = result.orElseThrow();
        // 만약 변경한다면, 제목과 내용만 변경하기. ,엔티티 클래스에서 메서드로 만듦.
        lunch.changeTitleAndContent(lunchDTO.getTitle(),lunchDTO.getContent());
        // 적용하기.
        lunchRepository.save(lunch);
    }

    @Override
    public void delete(Long mno) {
        // 레스트 방식일 때는 , 유효성 체크해서 없다는 메세지 전달해줘야 함.
//    Optional<Board> result = boardRepository.findById(bno);
//    Board board = result.orElseThrow();
        lunchRepository.deleteById(mno);
    }

    @Override
    public PageResponseDTO<LunchDTO> list(PageRequestDTO pageRequestDTO) {
        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("mno");
        // 검색어, 페이징 처리가 된 결과물 10개.
        // VO
        Page<Lunch> result = lunchRepository.searchAll(types,keyword,pageable);
        // Entity(VO) -> DTO
        List<LunchDTO> dtoList = result.getContent().stream()
                .map(lunch -> modelMapper.map(lunch,LunchDTO.class))
                .collect(Collectors.toList());

        // 서버 -> 화면에 전달할 준비물 준비 작업 완료.
        // 1)페이지 2) 사이즈 3) 전쳇갯수 4) 검색 결과 내역10개(엔티티-> DTO)
        PageResponseDTO pageResponseDTO = PageResponseDTO.<LunchDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int) result.getTotalElements())
                .build();

        return pageResponseDTO;
    }
}

package clap.server.domain.model.task;

import clap.server.domain.model.common.BaseTime;
import clap.server.domain.model.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseTime {
   private Long categoryId;
   private Member admin;
   private Category mainCategory;
   private String code; 
   private String name; 
   private boolean isDeleted;
   private String descriptionExample;
   private LocalDateTime createdAt; 
   private LocalDateTime updatedAt;
 }


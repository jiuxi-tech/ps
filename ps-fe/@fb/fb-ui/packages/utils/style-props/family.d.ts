/**
 * family
 * (c) 2024 lincong1987
 */

export interface FamilyProps {
  /** 字体 */
  family?: string;

  familyYahei?: boolean;

  familySans?: boolean;

  familySong?: boolean;

  familySerif?: boolean;

  familyMono?: boolean;

  familyCode?: boolean;

  familyEmoji?: boolean;

  familyFallback?: boolean;

  familyDingtalkJinbuti?: boolean;

  familyJinbuti?: boolean;

  familyFzzchjw?: boolean;

  familySansationLight?: boolean;

  familyYoushebiaotihei?: boolean;
}

export interface FamilyStyle {
  (): {
    fontFamily?: string;
  };
}

export const props: FamilyProps;

export const style: FamilyStyle;

export default {
  props,
  style,
};
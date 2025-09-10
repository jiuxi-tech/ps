/**
 * gridArea
 * (c) 2024 lincong1987
 */

export interface GridAreaProps {
  gridArea?: string;

  gridRow?: string;

  rowAuto?: boolean;

  rowSpan1?: boolean;

  rowSpan2?: boolean;

  rowSpan3?: boolean;

  rowSpan4?: boolean;

  rowSpan5?: boolean;

  rowSpan6?: boolean;

  rowSpan7?: boolean;

  rowSpan8?: boolean;

  rowSpan9?: boolean;

  rowSpan10?: boolean;

  rowSpan11?: boolean;

  rowSpan12?: boolean;

  rowSpanFull?: boolean;

  rowStart?: number | string;

  rowStart1?: boolean;

  rowStart2?: boolean;

  rowStart3?: boolean;

  rowStart4?: boolean;

  rowStart5?: boolean;

  rowStart6?: boolean;

  rowStart7?: boolean;

  rowStart8?: boolean;

  rowStart9?: boolean;

  rowStart10?: boolean;

  rowStart11?: boolean;

  rowStart12?: boolean;

  rowStart13?: boolean;

  rowStartFull?: boolean;

  rowStartAuto?: boolean;

  rowEnd?: number | string;

  rowEnd1?: boolean;

  rowEnd2?: boolean;

  rowEnd3?: boolean;

  rowEnd4?: boolean;

  rowEnd5?: boolean;

  rowEnd6?: boolean;

  rowEnd7?: boolean;

  rowEnd8?: boolean;

  rowEnd9?: boolean;

  rowEnd10?: boolean;

  rowEnd11?: boolean;

  rowEnd12?: boolean;

  rowEnd13?: boolean;

  rowEndAuto?: boolean;

  gridColumn?: string;

  colAuto?: boolean;

  colSpan1?: boolean;

  colSpan2?: boolean;

  colSpan3?: boolean;

  colSpan4?: boolean;

  colSpan5?: boolean;

  colSpan6?: boolean;

  colSpan7?: boolean;

  colSpan8?: boolean;

  colSpan9?: boolean;

  colSpan10?: boolean;

  colSpan11?: boolean;

  colSpan12?: boolean;

  colSpanFull?: boolean;

  colStart?: number | string;

  colStart1?: boolean;

  colStart2?: boolean;

  colStart3?: boolean;

  colStart4?: boolean;

  colStart5?: boolean;

  colStart6?: boolean;

  colStart7?: boolean;

  colStart8?: boolean;

  colStart9?: boolean;

  colStart10?: boolean;

  colStart11?: boolean;

  colStart12?: boolean;

  colStart13?: boolean;

  colStartAuto?: boolean;

  colEnd?: number | string;

  colEnd1?: boolean;

  colEnd2?: boolean;

  colEnd3?: boolean;

  colEnd4?: boolean;

  colEnd5?: boolean;

  colEnd6?: boolean;

  colEnd7?: boolean;

  colEnd8?: boolean;

  colEnd9?: boolean;

  colEnd10?: boolean;

  colEnd11?: boolean;

  colEnd12?: boolean;

  colEnd13?: boolean;

  colEndAuto?: boolean;
}

export interface GridAreaStyle {
  (): {
    gridArea?: string;
    gridRow?: string;
    gridRowStart?: string;
    gridRowEnd?: string;
    gridColumn?: string;
    gridColumnStart?: string;
    gridColumnEnd?: string;
  };
}

export const props: GridAreaProps;

export const style: GridAreaStyle;

export default {
  props,
  style,
};
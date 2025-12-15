import React from 'react';

// 声明 Window 接口扩展，添加自定义属性
declare global {
  interface Window {
    logoutButtonClickTimeoutId?: NodeJS.Timeout;
  }
}

// 自定义党政风格 Template：保持关键 ID，插入党政风格容器与标题结构
// 为兼容性与快速迭代，暂用 any 作为 props 类型，主要渲染 children
// eslint-disable-next-line @typescript-eslint/no-explicit-any
export default function Template(props: any) {
  const { children } = props;

  // 初始化脚本
  React.useEffect(() => {
    document.title = '统一身份认证 - 中共陕西省委党校（陕西行政学院）';
    setTimeout(function() {
      const loginForm = document.getElementById('kc-form-login') as HTMLFormElement;
      if (loginForm && loginForm.action) {
        loginForm.action = loginForm.action.replace(/^http:/, '');
 
        // 创建浮动二维码
        const qrcodeSvg = `<svg xmlns="http://www.w3.org/2000/svg" version="1.1" width="254" height="254" viewBox="0 0 254 254">
  <rect x="0" y="0" width="254" height="254" style="fill:#ffffff;shape-rendering:crispEdges;"></rect>
  <rect x="15.393939393939394" y="15.393939393939394" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="23.090909090909093" y="15.393939393939394" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="30.78787878787879" y="15.393939393939394" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="38.484848484848484" y="15.393939393939394" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="46.18181818181819" y="15.393939393939394" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="53.878787878787875" y="15.393939393939394" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="61.57575757575758" y="15.393939393939394" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="84.66666666666667" y="15.393939393939394" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="100.06060606060606" y="15.393939393939394" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="107.75757575757576" y="15.393939393939394" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="123.15151515151516" y="15.393939393939394" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="130.84848484848484" y="15.393939393939394" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="146.24242424242425" y="15.393939393939394" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="169.33333333333334" y="15.393939393939394" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="184.72727272727275" y="15.393939393939394" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="192.42424242424244" y="15.393939393939394" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="200.12121212121215" y="15.393939393939394" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="207.81818181818184" y="15.393939393939394" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="215.51515151515153" y="15.393939393939394" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="223.21212121212122" y="15.393939393939394" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="230.90909090909093" y="15.393939393939394" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="15.393939393939394" y="23.090909090909093" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="61.57575757575758" y="23.090909090909093" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="76.96969696969697" y="23.090909090909093" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="92.36363636363636" y="23.090909090909093" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="100.06060606060606" y="23.090909090909093" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="130.84848484848484" y="23.090909090909093" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="138.54545454545456" y="23.090909090909093" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="146.24242424242425" y="23.090909090909093" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="169.33333333333334" y="23.090909090909093" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="184.72727272727275" y="23.090909090909093" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="230.90909090909093" y="23.090909090909093" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="15.393939393939394" y="30.78787878787879" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="30.78787878787879" y="30.78787878787879" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="38.484848484848484" y="30.78787878787879" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="46.18181818181819" y="30.78787878787879" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="61.57575757575758" y="30.78787878787879" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="92.36363636363636" y="30.78787878787879" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="107.75757575757576" y="30.78787878787879" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="115.45454545454545" y="30.78787878787879" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="130.84848484848484" y="30.78787878787879" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="138.54545454545456" y="30.78787878787879" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="146.24242424242425" y="30.78787878787879" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="153.93939393939397" y="30.78787878787879" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="169.33333333333334" y="30.78787878787879" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="184.72727272727275" y="30.78787878787879" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="200.12121212121215" y="30.78787878787879" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="207.81818181818184" y="30.78787878787879" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="215.51515151515153" y="30.78787878787879" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="230.90909090909093" y="30.78787878787879" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="15.393939393939394" y="38.484848484848484" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="30.78787878787879" y="38.484848484848484" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="38.484848484848484" y="38.484848484848484" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="46.18181818181819" y="38.484848484848484" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="61.57575757575758" y="38.484848484848484" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="92.36363636363636" y="38.484848484848484" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="115.45454545454545" y="38.484848484848484" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="130.84848484848484" y="38.484848484848484" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="138.54545454545456" y="38.484848484848484" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="161.63636363636365" y="38.484848484848484" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="184.72727272727275" y="38.484848484848484" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="200.12121212121215" y="38.484848484848484" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="207.81818181818184" y="38.484848484848484" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="215.51515151515153" y="38.484848484848484" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="230.90909090909093" y="38.484848484848484" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="15.393939393939394" y="46.18181818181819" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="30.78787878787879" y="46.18181818181819" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="38.484848484848484" y="46.18181818181819" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="46.18181818181819" y="46.18181818181819" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="61.57575757575758" y="46.18181818181819" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="76.96969696969697" y="46.18181818181819" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="107.75757575757576" y="46.18181818181819" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="115.45454545454545" y="46.18181818181819" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="123.15151515151516" y="46.18181818181819" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="138.54545454545456" y="46.18181818181819" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="146.24242424242425" y="46.18181818181819" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="161.63636363636365" y="46.18181818181819" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="169.33333333333334" y="46.18181818181819" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="184.72727272727275" y="46.18181818181819" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="200.12121212121215" y="46.18181818181819" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="207.81818181818184" y="46.18181818181819" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="215.51515151515153" y="46.18181818181819" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="230.90909090909093" y="46.18181818181819" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="15.393939393939394" y="53.878787878787875" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="61.57575757575758" y="53.878787878787875" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="92.36363636363636" y="53.878787878787875" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="115.45454545454545" y="53.878787878787875" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="123.15151515151516" y="53.878787878787875" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="169.33333333333334" y="53.878787878787875" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="184.72727272727275" y="53.878787878787875" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="230.90909090909093" y="53.878787878787875" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="15.393939393939394" y="61.57575757575758" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="23.090909090909093" y="61.57575757575758" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="30.78787878787879" y="61.57575757575758" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="38.484848484848484" y="61.57575757575758" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="46.18181818181819" y="61.57575757575758" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="53.878787878787875" y="61.57575757575758" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="61.57575757575758" y="61.57575757575758" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="76.96969696969697" y="61.57575757575758" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="92.36363636363636" y="61.57575757575758" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="107.75757575757576" y="61.57575757575758" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="123.15151515151516" y="61.57575757575758" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="138.54545454545456" y="61.57575757575758" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="153.93939393939397" y="61.57575757575758" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="169.33333333333334" y="61.57575757575758" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="184.72727272727275" y="61.57575757575758" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="192.42424242424244" y="61.57575757575758" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="200.12121212121215" y="61.57575757575758" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="207.81818181818184" y="61.57575757575758" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="215.51515151515153" y="61.57575757575758" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="223.21212121212122" y="61.57575757575758" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="230.90909090909093" y="61.57575757575758" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="84.66666666666667" y="69.27272727272728" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="100.06060606060606" y="69.27272727272728" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="115.45454545454545" y="69.27272727272728" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="130.84848484848484" y="69.27272727272728" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="146.24242424242425" y="69.27272727272728" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="153.93939393939397" y="69.27272727272728" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="30.78787878787879" y="76.96969696969697" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="46.18181818181819" y="76.96969696969697" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="53.878787878787875" y="76.96969696969697" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="61.57575757575758" y="76.96969696969697" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="76.96969696969697" y="76.96969696969697" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="84.66666666666667" y="76.96969696969697" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="123.15151515151516" y="76.96969696969697" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="130.84848484848484" y="76.96969696969697" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="146.24242424242425" y="76.96969696969697" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="161.63636363636365" y="76.96969696969697" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="169.33333333333334" y="76.96969696969697" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="177.03030303030306" y="76.96969696969697" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="200.12121212121215" y="76.96969696969697" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="223.21212121212122" y="76.96969696969697" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="23.090909090909093" y="84.66666666666667" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="30.78787878787879" y="84.66666666666667" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="38.484848484848484" y="84.66666666666667" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="46.18181818181819" y="84.66666666666667" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="53.878787878787875" y="84.66666666666667" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="76.96969696969697" y="84.66666666666667" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="100.06060606060606" y="84.66666666666667" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="115.45454545454545" y="84.66666666666667" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="123.15151515151516" y="84.66666666666667" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="130.84848484848484" y="84.66666666666667" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="146.24242424242425" y="84.66666666666667" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="161.63636363636365" y="84.66666666666667" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="184.72727272727275" y="84.66666666666667" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="200.12121212121215" y="84.66666666666667" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="207.81818181818184" y="84.66666666666667" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="230.90909090909093" y="84.66666666666667" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="15.393939393939394" y="92.36363636363636" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="38.484848484848484" y="92.36363636363636" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="46.18181818181819" y="92.36363636363636" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="61.57575757575758" y="92.36363636363636" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="76.96969696969697" y="92.36363636363636" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="84.66666666666667" y="92.36363636363636" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="107.75757575757576" y="92.36363636363636" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="115.45454545454545" y="92.36363636363636" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="123.15151515151516" y="92.36363636363636" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="130.84848484848484" y="92.36363636363636" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="138.54545454545456" y="92.36363636363636" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="146.24242424242425" y="92.36363636363636" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="161.63636363636365" y="92.36363636363636" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="177.03030303030306" y="92.36363636363636" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="200.12121212121215" y="92.36363636363636" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="223.21212121212122" y="92.36363636363636" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="230.90909090909093" y="92.36363636363636" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="15.393939393939394" y="100.06060606060606" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="30.78787878787879" y="100.06060606060606" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="38.484848484848484" y="100.06060606060606" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="46.18181818181819" y="100.06060606060606" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="53.878787878787875" y="100.06060606060606" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="76.96969696969697" y="100.06060606060606" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="84.66666666666667" y="100.06060606060606" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="92.36363636363636" y="100.06060606060606" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="107.75757575757576" y="100.06060606060606" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="146.24242424242425" y="100.06060606060606" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="153.93939393939397" y="100.06060606060606" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="184.72727272727275" y="100.06060606060606" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="192.42424242424244" y="100.06060606060606" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="207.81818181818184" y="100.06060606060606" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="215.51515151515153" y="100.06060606060606" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="30.78787878787879" y="107.75757575757576" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="38.484848484848484" y="107.75757575757576" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="61.57575757575758" y="107.75757575757576" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="76.96969696969697" y="107.75757575757576" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="92.36363636363636" y="107.75757575757576" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="115.45454545454545" y="107.75757575757576" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="138.54545454545456" y="107.75757575757576" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="146.24242424242425" y="107.75757575757576" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="169.33333333333334" y="107.75757575757576" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="192.42424242424244" y="107.75757575757576" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="215.51515151515153" y="107.75757575757576" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="15.393939393939394" y="115.45454545454545" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="38.484848484848484" y="115.45454545454545" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="53.878787878787875" y="115.45454545454545" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="84.66666666666667" y="115.45454545454545" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="92.36363636363636" y="115.45454545454545" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="100.06060606060606" y="115.45454545454545" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="107.75757575757576" y="115.45454545454545" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="115.45454545454545" y="115.45454545454545" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="130.84848484848484" y="115.45454545454545" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="184.72727272727275" y="115.45454545454545" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="192.42424242424244" y="115.45454545454545" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="200.12121212121215" y="115.45454545454545" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="207.81818181818184" y="115.45454545454545" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="23.090909090909093" y="123.15151515151516" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="38.484848484848484" y="123.15151515151516" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="46.18181818181819" y="123.15151515151516" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="53.878787878787875" y="123.15151515151516" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="61.57575757575758" y="123.15151515151516" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="69.27272727272728" y="123.15151515151516" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="76.96969696969697" y="123.15151515151516" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="92.36363636363636" y="123.15151515151516" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="107.75757575757576" y="123.15151515151516" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="115.45454545454545" y="123.15151515151516" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="123.15151515151516" y="123.15151515151516" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="130.84848484848484" y="123.15151515151516" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="146.24242424242425" y="123.15151515151516" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="161.63636363636365" y="123.15151515151516" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="169.33333333333334" y="123.15151515151516" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="177.03030303030306" y="123.15151515151516" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="184.72727272727275" y="123.15151515151516" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="200.12121212121215" y="123.15151515151516" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="207.81818181818184" y="123.15151515151516" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="223.21212121212122" y="123.15151515151516" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="15.393939393939394" y="130.84848484848484" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="69.27272727272728" y="130.84848484848484" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="92.36363636363636" y="130.84848484848484" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="100.06060606060606" y="130.84848484848484" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="115.45454545454545" y="130.84848484848484" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="123.15151515151516" y="130.84848484848484" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="138.54545454545456" y="130.84848484848484" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="153.93939393939397" y="130.84848484848484" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="161.63636363636365" y="130.84848484848484" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="177.03030303030306" y="130.84848484848484" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="184.72727272727275" y="130.84848484848484" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="192.42424242424244" y="130.84848484848484" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="200.12121212121215" y="130.84848484848484" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="230.90909090909093" y="130.84848484848484" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="15.393939393939394" y="138.54545454545456" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="23.090909090909093" y="138.54545454545456" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="30.78787878787879" y="138.54545454545456" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="46.18181818181819" y="138.54545454545456" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="61.57575757575758" y="138.54545454545456" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="76.96969696969697" y="138.54545454545456" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="107.75757575757576" y="138.54545454545456" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="115.45454545454545" y="138.54545454545456" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="123.15151515151516" y="138.54545454545456" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="130.84848484848484" y="138.54545454545456" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="138.54545454545456" y="138.54545454545456" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="161.63636363636365" y="138.54545454545456" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="169.33333333333334" y="138.54545454545456" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="177.03030303030306" y="138.54545454545456" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="184.72727272727275" y="138.54545454545456" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="192.42424242424244" y="138.54545454545456" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="207.81818181818184" y="138.54545454545456" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="215.51515151515153" y="138.54545454545456" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="30.78787878787879" y="146.24242424242425" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="69.27272727272728" y="146.24242424242425" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="100.06060606060606" y="146.24242424242425" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="107.75757575757576" y="146.24242424242425" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="115.45454545454545" y="146.24242424242425" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="146.24242424242425" y="146.24242424242425" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="161.63636363636365" y="146.24242424242425" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="169.33333333333334" y="146.24242424242425" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="184.72727272727275" y="146.24242424242425" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="192.42424242424244" y="146.24242424242425" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="223.21212121212122" y="146.24242424242425" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="230.90909090909093" y="146.24242424242425" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="15.393939393939394" y="153.93939393939397" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="38.484848484848484" y="153.93939393939397" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="46.18181818181819" y="153.93939393939397" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="53.878787878787875" y="153.93939393939397" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="61.57575757575758" y="153.93939393939397" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="69.27272727272728" y="153.93939393939397" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="76.96969696969697" y="153.93939393939397" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="107.75757575757576" y="153.93939393939397" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="115.45454545454545" y="153.93939393939397" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="123.15151515151516" y="153.93939393939397" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="146.24242424242425" y="153.93939393939397" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="153.93939393939397" y="153.93939393939397" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="192.42424242424244" y="153.93939393939397" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="200.12121212121215" y="153.93939393939397" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="223.21212121212122" y="153.93939393939397" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="15.393939393939394" y="161.63636363636365" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="30.78787878787879" y="161.63636363636365" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="38.484848484848484" y="161.63636363636365" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="53.878787878787875" y="161.63636363636365" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="69.27272727272728" y="161.63636363636365" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="76.96969696969697" y="161.63636363636365" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="84.66666666666667" y="161.63636363636365" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="107.75757575757576" y="161.63636363636365" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="146.24242424242425" y="161.63636363636365" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="153.93939393939397" y="161.63636363636365" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="161.63636363636365" y="161.63636363636365" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="184.72727272727275" y="161.63636363636365" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="192.42424242424244" y="161.63636363636365" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="200.12121212121215" y="161.63636363636365" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="215.51515151515153" y="161.63636363636365" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="223.21212121212122" y="161.63636363636365" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="230.90909090909093" y="161.63636363636365" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="23.090909090909093" y="169.33333333333334" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="30.78787878787879" y="169.33333333333334" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="38.484848484848484" y="169.33333333333334" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="61.57575757575758" y="169.33333333333334" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="69.27272727272728" y="169.33333333333334" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="84.66666666666667" y="169.33333333333334" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="92.36363636363636" y="169.33333333333334" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="123.15151515151516" y="169.33333333333334" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="138.54545454545456" y="169.33333333333334" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="161.63636363636365" y="169.33333333333334" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="169.33333333333334" y="169.33333333333334" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="177.03030303030306" y="169.33333333333334" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="184.72727272727275" y="169.33333333333334" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="192.42424242424244" y="169.33333333333334" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="200.12121212121215" y="169.33333333333334" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="215.51515151515153" y="169.33333333333334" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="230.90909090909093" y="169.33333333333334" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="76.96969696969697" y="177.03030303030306" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="92.36363636363636" y="177.03030303030306" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="100.06060606060606" y="177.03030303030306" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="130.84848484848484" y="177.03030303030306" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="138.54545454545456" y="177.03030303030306" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="169.33333333333334" y="177.03030303030306" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="200.12121212121215" y="177.03030303030306" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="215.51515151515153" y="177.03030303030306" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="223.21212121212122" y="177.03030303030306" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="230.90909090909093" y="177.03030303030306" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="15.393939393939394" y="184.72727272727275" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="23.090909090909093" y="184.72727272727275" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="30.78787878787879" y="184.72727272727275" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="38.484848484848484" y="184.72727272727275" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="46.18181818181819" y="184.72727272727275" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="53.878787878787875" y="184.72727272727275" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="61.57575757575758" y="184.72727272727275" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="115.45454545454545" y="184.72727272727275" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="138.54545454545456" y="184.72727272727275" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="169.33333333333334" y="184.72727272727275" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="184.72727272727275" y="184.72727272727275" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="200.12121212121215" y="184.72727272727275" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="223.21212121212122" y="184.72727272727275" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="15.393939393939394" y="192.42424242424244" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="61.57575757575758" y="192.42424242424244" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="76.96969696969697" y="192.42424242424244" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="107.75757575757576" y="192.42424242424244" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="130.84848484848484" y="192.42424242424244" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="146.24242424242425" y="192.42424242424244" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="169.33333333333334" y="192.42424242424244" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="200.12121212121215" y="192.42424242424244" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="207.81818181818184" y="192.42424242424244" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="223.21212121212122" y="192.42424242424244" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="15.393939393939394" y="200.12121212121215" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="30.78787878787879" y="200.12121212121215" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="38.484848484848484" y="200.12121212121215" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="46.18181818181819" y="200.12121212121215" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="61.57575757575758" y="200.12121212121215" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="76.96969696969697" y="200.12121212121215" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="84.66666666666667" y="200.12121212121215" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="100.06060606060606" y="200.12121212121215" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="115.45454545454545" y="200.12121212121215" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="123.15151515151516" y="200.12121212121215" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="146.24242424242425" y="200.12121212121215" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="153.93939393939397" y="200.12121212121215" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="161.63636363636365" y="200.12121212121215" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="169.33333333333334" y="200.12121212121215" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="177.03030303030306" y="200.12121212121215" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="184.72727272727275" y="200.12121212121215" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="192.42424242424244" y="200.12121212121215" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="200.12121212121215" y="200.12121212121215" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="230.90909090909093" y="200.12121212121215" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="15.393939393939394" y="207.81818181818184" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="30.78787878787879" y="207.81818181818184" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="38.484848484848484" y="207.81818181818184" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="46.18181818181819" y="207.81818181818184" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="61.57575757575758" y="207.81818181818184" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="84.66666666666667" y="207.81818181818184" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="107.75757575757576" y="207.81818181818184" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="130.84848484848484" y="207.81818181818184" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="161.63636363636365" y="207.81818181818184" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="169.33333333333334" y="207.81818181818184" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="192.42424242424244" y="207.81818181818184" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="207.81818181818184" y="207.81818181818184" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="215.51515151515153" y="207.81818181818184" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="223.21212121212122" y="207.81818181818184" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="230.90909090909093" y="207.81818181818184" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="15.393939393939394" y="215.51515151515153" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="30.78787878787879" y="215.51515151515153" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="38.484848484848484" y="215.51515151515153" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="46.18181818181819" y="215.51515151515153" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="61.57575757575758" y="215.51515151515153" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="76.96969696969697" y="215.51515151515153" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="115.45454545454545" y="215.51515151515153" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="153.93939393939397" y="215.51515151515153" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="177.03030303030306" y="215.51515151515153" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="207.81818181818184" y="215.51515151515153" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="215.51515151515153" y="215.51515151515153" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="15.393939393939394" y="223.21212121212122" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="61.57575757575758" y="223.21212121212122" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="84.66666666666667" y="223.21212121212122" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="100.06060606060606" y="223.21212121212122" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="115.45454545454545" y="223.21212121212122" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="130.84848484848484" y="223.21212121212122" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="138.54545454545456" y="223.21212121212122" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="161.63636363636365" y="223.21212121212122" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="169.33333333333334" y="223.21212121212122" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="192.42424242424244" y="223.21212121212122" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="207.81818181818184" y="223.21212121212122" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="215.51515151515153" y="223.21212121212122" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="223.21212121212122" y="223.21212121212122" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="230.90909090909093" y="223.21212121212122" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="15.393939393939394" y="230.90909090909093" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="23.090909090909093" y="230.90909090909093" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="30.78787878787879" y="230.90909090909093" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="38.484848484848484" y="230.90909090909093" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="46.18181818181819" y="230.90909090909093" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="53.878787878787875" y="230.90909090909093" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="61.57575757575758" y="230.90909090909093" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="84.66666666666667" y="230.90909090909093" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="92.36363636363636" y="230.90909090909093" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="100.06060606060606" y="230.90909090909093" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="107.75757575757576" y="230.90909090909093" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="115.45454545454545" y="230.90909090909093" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="123.15151515151516" y="230.90909090909093" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="130.84848484848484" y="230.90909090909093" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="146.24242424242425" y="230.90909090909093" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="153.93939393939397" y="230.90909090909093" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="177.03030303030306" y="230.90909090909093" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="184.72727272727275" y="230.90909090909093" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="192.42424242424244" y="230.90909090909093" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="200.12121212121215" y="230.90909090909093" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="215.51515151515153" y="230.90909090909093" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
  <rect x="223.21212121212122" y="230.90909090909093" width="7.696969696969697" height="7.696969696969697" style="fill:#000000;shape-rendering:crispEdges;"></rect>
</svg>`

        // 创建浮动二维码容器
        const qrFloatContainer = document.createElement('div');
        qrFloatContainer.id = 'qr-float-container';
        qrFloatContainer.style.cssText = `
          position: fixed;
          top: 10px;
          right: 10px;
          width: 75px;
          height: 75px;
          background: white;
          border-radius: 8px;
          box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
          cursor: pointer;
          z-index: 9998;
          transition: all 0.3s ease;
          overflow: hidden;
        `;
        qrFloatContainer.innerHTML = qrcodeSvg;
        
        // SVG 自适应容器大小
        const svgElement = qrFloatContainer.querySelector('svg');
        if (svgElement) {
          svgElement.style.width = '100%';
          svgElement.style.height = '100%';
        }
        
        // 创建遮罩层和中置二维码容器
        const qrModalOverlay = document.createElement('div');
        qrModalOverlay.id = 'qr-modal-overlay';
        qrModalOverlay.style.cssText = `
          position: fixed;
          top: 0;
          left: 0;
          right: 0;
          bottom: 0;
          background: rgba(0, 0, 0, 0.6);
          display: none;
          justify-content: center;
          align-items: center;
          z-index: 9999;
        `;
        
        const qrModalContainer = document.createElement('div');
        qrModalContainer.style.cssText = `
          position: relative;
          width: 240px;
          height: 240px;
          background: white;
          border-radius: 12px;
          box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
          padding: 10px;
        `;
        
        const qrModalContent = document.createElement('div');
        qrModalContent.style.cssText = `
          width: 100%;
          height: 100%;
        `;
        qrModalContent.innerHTML = qrcodeSvg;
        
        // 中置二维码的 SVG 自适应
        const modalSvg = qrModalContent.querySelector('svg');
        if (modalSvg) {
          modalSvg.style.width = '100%';
          modalSvg.style.height = '100%';
        }
        
        // 创建关闭按钮
        const closeButton = document.createElement('button');
        closeButton.innerHTML = '×';
        closeButton.style.cssText = `
          position: absolute;
          top: -10px;
          right: -10px;
          width: 32px;
          height: 32px;
          border-radius: 50%;
          background: #ff4d4f;
          color: white;
          border: 2px solid white;
          font-size: 20px;
          line-height: 1;
          cursor: pointer;
          display: flex;
          align-items: center;
          justify-content: center;
          box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
          transition: all 0.2s ease;
        `;
        
        closeButton.addEventListener('mouseenter', () => {
          closeButton.style.background = '#d9363e';
          closeButton.style.transform = 'scale(1.1)';
        });
        
        closeButton.addEventListener('mouseleave', () => {
          closeButton.style.background = '#ff4d4f';
          closeButton.style.transform = 'scale(1)';
        });
        
        closeButton.addEventListener('click', (e) => {
          e.stopPropagation();
          qrModalOverlay.style.display = 'none';
        });
        
        // 组装模态框
        qrModalContainer.appendChild(qrModalContent);
        qrModalContainer.appendChild(closeButton);
        qrModalOverlay.appendChild(qrModalContainer);
        
        // 点击浮动二维码显示中置模态框
        qrFloatContainer.addEventListener('click', () => {
          qrModalOverlay.style.display = 'flex';
        });
        
        // 点击遮罩层关闭
        qrModalOverlay.addEventListener('click', (e) => {
          if (e.target === qrModalOverlay) {
            qrModalOverlay.style.display = 'none';
          }
        });
        
        // 添加到页面
        document.body.appendChild(qrFloatContainer);
        document.body.appendChild(qrModalOverlay);
      }


      // input#username 添加 placeholder
      const usernameInput = document.getElementById('username') as HTMLInputElement;
      if (usernameInput) {
        usernameInput.placeholder = '账号/手机号/身份证号';
      }

      // input#password 添加 placeholder
      const passwordInput = document.getElementById('password') as HTMLInputElement;
      if (passwordInput) {
        passwordInput.placeholder = '密码';
      }
      


      const usernameOrEmail = document.querySelector('[data-kc-msg="usernameOrEmail"]') as HTMLSpanElement;
      if (usernameOrEmail) {
        usernameOrEmail.textContent = '账号';
      }

      //if (window.kcContext?.pageId === 'logout-confirm.ftl') {
        const logoutButton = document.getElementById('kc-logout') as HTMLInputElement;
         
        if (logoutButton) {
          logoutButton.value = '自动注销中...';
          if( window. logoutButtonClickTimeoutId){

              clearTimeout( window. logoutButtonClickTimeoutId);
          }
           window. logoutButtonClickTimeoutId = setTimeout(function() {
            logoutButton.click();
          }, 5000);

          const cancelButton = document.getElementById('kc-cancel-logout') as HTMLInputElement;
          if(! cancelButton  ){

            // 添加取消按钮
            const cancelButton = document.createElement('input');
            cancelButton.type = 'button';
            cancelButton.name = 'cancelLogout';
            cancelButton.id = 'kc-cancel-logout';
            cancelButton.value = '取消';
            cancelButton.className = 'kcButtonClass pf-c-button kcButtonPrimaryClass pf-m-primary kcButtonBlockClass pf-m-block kcButtonLargeClass btn-lg';
            cancelButton.tabIndex = 5;
            logoutButton.form?.appendChild(cancelButton);

            cancelButton.addEventListener('click', function() {
              clearTimeout( window. logoutButtonClickTimeoutId);
              cancelButton.style.display = 'none';
              logoutButton.value = '注销';
            });
        
        }
     // }



      }

    }, 500);
  }, []);

  return (
    <div id="kc-page" className="pf-c-login">
      <header id="kc-header">
        {/* 兼容默认 wrapper（保留但隐藏） */}
        <div id="kc-header-wrapper" aria-hidden="true"></div>
        {/* 英雄横幅：图标 + 标题 */}
        <div className="party-hero" role="banner">
          <div className="party-hero-inner">
            <h1 className="party-hero-title">中共陕西省委党校（陕西行政学院）</h1>
            <h2 className="party-hero-subtitle">业务中台 · 统一身份认证</h2>
          </div>
        </div>
      </header>

      <main id="kc-page-content" role="main">
        <div className="party-login-container" id="kc-content">
          {/* 可选 Logo 区域（如 resources/img/logo.png 存在则显示） */}
          {/* <div className="party-logo-wrapper">
            <img className="party-logo" src={"resources/img/logo.png"} alt="Logo" />
          </div> */}

          {/* 主标题（若改为 DOM 文案，可启用以下内容替代伪元素） */}
          {/* <div className="party-title">
            中共陕西省委党校（陕西行政学院）业务中台
            <span className="subtitle">统一身份认证</span>
          </div> */}

          {/* 默认页面内容（表单、操作等） */}
          {children}
          
          {/* 安全提示信息 */}
          <div className="security-notice">
            <p className="security-notice-line">本平台为内网非涉密平台</p>
            <p className="security-notice-line">严禁处理、传输涉密信息</p>
          </div>
        </div>
      </main>

      {/* 底部版权信息 */}
      <footer className="party-footer" role="contentinfo">
        Copyright © 2025 中共陕西省委党校（陕西行政学院） 版权所有
      </footer>
    </div>
  );
}
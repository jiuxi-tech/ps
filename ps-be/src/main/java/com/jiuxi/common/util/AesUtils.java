package com.jiuxi.common.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;

/**
 * @ClassName: AesUtils
 * @Description: AesUtils 基于hutool上加密工具类封装
 * @Author: 杨攀
 * @Date: 2020/5/26 13:23
 * @Copyright: 2020 www.tuxun.net Inc. All rights reserved.
 */
public class AesUtils {

    /**
     * 对象
     */
    private volatile static AES aes;

    /**
     * 秘钥： 字符串长度不能小于16
     * private static String SECRET_KEY = "uKzE3CCNZ5XFwHwW";
     * <p>
     * 盐： 字符串长度不能小于16
     * private static String SECRET_IV = "zGrVju3LPhyhiJR8";
     */

    public static AES newInstance(String SECRET_KEY, String SECRET_IV) {
        if (aes == null) {
            synchronized (AesUtils.class) {
                if (aes == null) {
                    byte[] key = SECRET_KEY.getBytes();
                    byte[] vi = SECRET_IV.getBytes();
                    aes = new AES(Mode.CBC, Padding.PKCS5Padding, key, vi);
                }
            }
        }
        return aes;
    }


    /**
     * @param content 需要加密的内容
     * @return java.lang.String
     * @description: 加密为十六进制
     * @author 杨攀
     * @date 2020/5/26 13:51
     */
    public static String encryptHex(String content) {
        return aes.encryptHex(content);
    }


    /**
     * @param encrypt 十六进制的密文
     * @return java.lang.String
     * @description: 解密十六进制的密文
     * @author 杨攀
     * @date 2020/5/26 13:52
     */
    public static String decryptStr(String encrypt) {
        try {
            return aes.decryptStr(encrypt);
        } catch (Exception e) {
            throw new IllegalArgumentException("非法参数异常！");
        }
    }


    /*public static void main(String[] args) {

        AES aes = new AES(Mode.CBC, Padding.PKCS5Padding, Base64.decode("X7ZIUtahLLv+aUxCmNFmeA=="), Base64.decode("Bf4fbe5t07iJ7Ag6fJk34g=="));
        String aaa = aes.decryptStr("4Oh6HaxIPuc0pfrmol9bUZrrrd9NJVKPssGGI2Pv7+YhcX3vxS6mK+oZjTQMJtRn6P4L55tncs77hOtj+fjJhezv66FaRooT021RkJ/ILVW2WCiHvRZ4zLAtm9tbSvyKfDqDc3VbP9HnuDKjrG6JHIx/5KB2bZtiVimxKgYXzoqXNgaTZvzCv4qLhEuoKc22i4S/dZY+J4Fp8N/POC3flfYyETXsr/pj0D6516UVj+OCOwV3EHuLv6GHCAdc39o3G+eRoqMkg4InpTKBTCioxAwPPaUg5Ix8LaTq3CrLPPRC4t3TkKIyhkVtc5O+71dWMddO7TS2ymDGIYH8kYJSnUYtg8n6fNXHeOnmoGVmPIO6F3orx+EMVen5Wpn22JUs");
        System.out.println(aaa);

        System.out.println(aes.encrypt(aaa));
    }*/

    public static void main(String[] args) {
        /**
         * 加密key
         */
        String key = "X7ZIUtahLLv+aUxCmNFmeA==";
        /**
         * 加密盐
         */
        /*String iv = "Bf4fbe5t07iJ7Ag6fJk34g==";
        String[] aa = {"T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JNAetSwvN/NH6VD1Q2ICf1kvCjrLPHdYds3GaT6UEjxE5qhAUGVU1j32CfoiLqNZ010ogtv5ParPKKeh0e0zrvUruWO8EFyZfhpANjYI9j2khV8XnwUBkj0BpNZo9m96mm3G1Fi2+dQQaTdvBYN+QuLeWQaCGanuu0UaiklznMN/R0eg5p2a6kEpnvtz3PO1PS88xxep3YJE9GEQdRyJWaC",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JMaCGe0PV0HthvmwAx5MN1njGvb2KKb5nBcRhcf2NRiZubPRetZEMJnj67byNQXihyOIxbTYU6ea+wUdsbO8bAKv0ba7cYnKL8vaoX0UxN7ysN29Cy755og4Joi+KxRVoP+C/U5Sn1QOK5hkwYM/VfE1ILiB5tnSzwmOAgtUYKRRlcVTYJtDqp8blge6HpIbdrp4p2KPc3um43KHRZoSiQu",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JNXZq1YKEhg1YfWEB7C1yDQYkzGb7YYFTMlCHsY8fzYSzXErHQ+0KS3uyubAS1y5KdtLwQd252p3RfIXZKa7Ny8RPr6WJaTYt5SywhFhfa984FIBV2LOrzfsS7SaMnyGoGTFgUsKm9Knsy1NjHt1s9j/YgRZchnhiP9VwCzlu7xw2A62DC1GJahWhaKu9QRq4QtM1TdvHxOnbOqOsDyrXKU",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JM7Bg1jgNnlJYQcECzFvzZDuj5tnZOxENQnrUkLmIg4ATQiJmhYFEdvZqZtXweeKluS0vw+6pGJXJ55C+q08Rx+C/H9ul+1ilukdwUhkTmDRqhwnstodcp+XdmIS+2ujEE3C+q33E0n57Y2q77/z2FudVWr1Tu//BqmyWQhh2TnfxBypyEOVwsMK8D8rtuM4CLX3qjgTjHi19o7BYZIGKTO",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JPcWLkuZ8myZy1X8yqK81U347bBEVhipvZlxUADW6hjdrDwDL76tnC26sm308xQ44nmpZXOqjco8JMqYR21nJqYszsRI1Tx6UXHio6XJfnGfax43bCAJjVcLObVe/aVYAIp9Rrhu2zEOUdr+GDOQWbuwqIYMlXTiLxgNCyrE7w6kycU26eeY9/5HRJzhfekIy9kHAFskeWgCSq/IFdkZV4Q",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JOD+eShGbSU1rvORd1UECrkd4SpRQ6eUnTK+5g/ajX46kyzGbOXlKXeDa8L8vEilZVDwmox63sFKCMcH52VjpfN1quZfShmMmVU3mGVEfDi1G4EpHiOb+v/b63t32xivUauyi+V1DmBeDeQrLOnvAI/hwnYlQxJ2rYyI6bJKVXKF7q+ei8NsxlNdIrCpBO/8gaisMSdTO1PMjTFSE3z5vf1",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JMXasrae4QmynhL1rcnMQnENXoVzqJCaCIZaDHSmImx1RnZbAfP4ypmcgwutmVnfHTGlre0oKv+Sv4PeJW4u6jz5mw7TOBMoqWt4PdurJk/iVewyMXsOEj7gs+b+4TCZq88vBXql7gVWbTVKoETjXVrscgiSZwVtyl8T4h98DKBSj4jO6i9uEHHou/Q8QFGj7+CEwV9AqxgGzHV8SFS2iDo",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JMy+O2v26NC+HGtEcYVB3K6lczkewqc+G3EEx79wZaOcX7KdTGHaR7bUAydvv5qJ+lIlsdyBr/pkZeDmkJsfG1qoJ6pMKiW5BkJNhgUjtt8uA/QqKPGoJHRSPnVwRy9gYN/Y0dye47TM8qHBPPUWD1zqw3KdipL2z28RzvFmrMnsBdqQy64CszipmFuTkSkOyf0PK4Z3YnlCt9DLdOfWrAZ",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JPPYI933npWUTrDZH0RW9G+zfABQunMk1bsPWKUAcjj7MtkBnxuSr0hch5BMG9Y5mqspoqViOR0HDOsYG7VHNzHIFIBC61jVj2ufJe2BR9Qzi+XFcWaaYsHu9Zcbrr9jEk8Wh25d91EJPoOIjMcRDnABBhXZSI5a7DqYoPHzc/nPcG7IaLd0JDNsUi7fRwqKG1/QPQ6lNlVo4CiHlcchkUp",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JP5aMCyDiHctOIiXiRMEA63cP8gCBkOSY0mgNwFH83O7x3FKeovT/f6dss4PXq/xkOe3e3HJhj083a34cOjN+dWxCws+eMYJ6yLd1hYItMK+t97+nebea7myJX8WJZDGuj4zdn6gZKQxFrkMkm5/UXkMylSsPttdmNb9ikBge7olgGC0Erh3AAy7soZOKaWuidyjULeG8+hXZVNqlFImzc1",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JOK8WgIDgr1BN/kzA6xaUDK8LhSPFhc6aB+0stDk29n/AE41+s5vx7dFnJwESW6JU9VGSzfdDcb7Wlig18I0wiSgtIDJv524g4j/pzZkDyLTic5FmvBAiB0k61G8swK33gRqqvBFP9RfY5TVsEYiuL52zI5G//z6yBLMjatcXzYTbXzprcryIDxILGEOvFFNvnSXF/z/hMq3Pf9PFIgt4Wt",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JNrJHELt2THmCBwPWAil+FIlKBbvYktLgb4QlL5alX8PgAc61PLFtIBhFPxUMxVKKASk4DJ6I4ulUwhnt5vqZ8RJ3e5BJeWa1l9Bjqjy2+QWzLJZOhQD3sx2Nq4F7Pw5IINNK8wE1eXcEYHgV7XOt/yR18SlXHy3u0VQhK+joBr6Dp+edVTfBCoHA1SWKxDcGImLLYpyccqCPM+pONro5DX",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JOwkLRM3FusVVjkd6aeuvBzryEhQteVznp6UE9VvxoJUwUsRImanP3/QDgv3ja/mxJol68PUpEq4Mn0Mhh+uFVK+CN99RukZOe3y8739xe0HI7yBVOAM5sZamPiqUTTQDgjCTUlGkC5E1NZ8CX/IwsqQSwJtjm8SzQ4WGuqOQZfcSKZLCbA+lAUclkVCrwC3/yacQ4JdBZIgzaYWr0pkjPM",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JPdbJErfpEGjiVLy5s1ErpqGgwxSlDWiYk/GwCiKhl0f+xdxXP5ISon+FWCc6VxRICONKMeP/y1Ss71oJ2vyTMjEBerXKqo1/bLSAC6sHTxeRNNWif+N+/OOI9+bfqSGNCrJHnp6gqIfIqidWIfZsZOIbio0hCMyzn8VcmZ2XkfYYbRWWrRdVzr6ccUnOGEBQuUL42d8UytGrkMTRaKxj1R",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JPsJ7yWKWd/AKo+4y3kL2blYqT3EiTlsQ8VcQIp473TiluSSqEGp1qsjvr27RhidGjtxzM75Ez3nGc4OrVJQcEDUcfwMqzBNsmuQMTTvG6VAVst+etUQG22jPPhMhOSopTbbel/8opcOhuBobJN8ORg2Fgi8ag695TdLFKgV/zhtI/AwzfKpEcG/4cQWSBAX27o26NtHUE52r71mbW3t+3w",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JOc/C+IBIOK7xVNzcgyk7151qE1wbDhQ8COgDhCZphmr1cLRZxTgeqvdkUNLu+0N0bUzBLLgUCOX/BwIYlUiHtAFll/XNkuyRl3yNijTBUOYvKWB72jkiKlrnrNfu7mAjhGfo4w1qzJvQQ458xUMNbbmPEJCp9rCyFizcq1fVrF6VqqcigO1o2/NMtYKdC3JZXWFv7p6+XAD77FRR9sw6n8",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JMlDChEp3gUQVqBJL8ntDFdOYMa3zhZZY9lkl0bICeqtd50OFVza1OSFP+waAiLgcM8cmSWmHWV2ossrgtY1pQ5qVfWKgrMWBoNiOqh1xEE0NfyBQMJofJZF1iejM1BgRnbTEfCgjN8WaOZkk8rXzlRaHizbjgtRrEylMQiPycYfhqZM0p/d6R2PDX0sZCkNaEMH4Ia4vSzTQqGwCc/rfaI",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JMcl3qb2Oe9c4Ny0IWf6mOmKMOOyDhoqQllwRHxiB+QnsYWOy77paZ/ra2IftINSnynncL2iWEyWWg6MsshHsiqoSIBZPp9RbgFU0LpC+q5d/Jzb6jqJ3S2YlNfS+qfS9EThbjoBhWsAdPX1jBp6Q+fot5H/J+68+A5a8y+LaGHybvLpkR6zE3dk96yfmw+C/apQf82dkVuOMfa26Iyp+rO",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JPhQOJQ0CGgkHzD4z3GAHgr74bZq9agb+xINSfqpVggq7MNc5BbMkncZwZJ43Yi2mazVdyznaiqc9SP75WwYJwT5fDhK0PUw74Js9LcD8QpDwRakjMdideCoGTfnlNcwSKrqY7+oo4JV1mmkb2fgMBQjsznKykxzjUGW1JHLnkmEaTMY/G+bNFcrWUtv3zYSm4Xpe1Z8abb37DGLsT11evK",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JOrvGSRQBs149Co+bT7+QSAvLOZTefRQTHztPbmDfze6E735Mb3pkjz5PbEV0dkuglRiDp1oVGrUkX6QgyOOb5p5No0W6JtQRRkUmpuqvXsMRwpxL+ijI09Py32OX8pgFOHJVHjShwBsqwA2C+1bYPnj+a7boFSpq+B18wXVG5N6DjyxgJwu599tdC4fkisougZH2aML8G3UnnBnwUaqJH7",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JMEWZBPiPmO92CuLRxcTetfceJsaqxr2yrCsjB2iXwNjcpUHaGWCUAyBQU82X5Dz2J2iTYJRMCXqkDd4ChmKdin1Gh3Qrc8LxCVVA4ImscE83ZnKbMxRLpN11Mu35nqJraFqkstcd4PHR+eAjIkwstJUgdc8ANeRFShP6a+JSElAkbE9wOl4L/QxWOvsqBVfDQa/mbqTHKczUkq7AQh3VuX",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JMQT4OUxme+nP/qi8kpVU4+wjVnjYGV8bSOMXV46WrVUgED6ccdXFz1nCW7QaBbDhQwW7rUxpx9FtHZCjQpi4S96b7f6NfmvL1TmC3RND5mTvl5bwvllfwnvK5uossgkRLvm/azKoZYGCboyOV+ZuaaFrDusEkNKEd0vhvm6teBqGpGaaZSZbPiDdilw+asYciuQteLLhu0JdvwUiFVPAf7",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JOcAnXC/AUZ3Su7NWlK+XY4LFU6o6V9TDqO9797UeFHO94BVPuHBXYSucUUbBakStTOPYzI8NlwHcOQRGfm6+kKxx4HEETLZ1xZB14eWFzhpgAo3Y4qVgdPTX591jX/m6Vu4klVg6yA2XvVPnbCp9tI0JnVQ6gptLZzYUl6r1axrrHQd1DGdUcptjdKzBtW7NoS9WATG4gPHKug8sRydvTJ",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JOMCKCHgNXh12x38/moyCdekP6Ra+LZSKdy9FA77sNm5U+uTHkrTzI2o/hA18e64VPGNKytS+y32dgG5bEyU2r9aZexukSgdg2cPxgsDhBakkWCaurM/nrJBddYk/pAm8iV0eF0hwdpVGvtZi7po+w4TKiPhZYvM7Gg5cboU1DOihEnr/oIPJ9ALIoJUNcIOxphlSKZEnP470e8Bgw3yfbu",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JNbqDmnnWoL02XlELAwnUdXuxjzEDDF6VmyjkQMNo2eUoYMe/xaFjZFpor2skhzN7bRnI5q2pcv4bp4Mi7YpgFnRw7bkIubTRegdq5qk082u7Pa31fTWxFfffh5Z/4+GHfXoyid1D8LK3jmg0QDZVqOlIjBF1sK3reJfuxsSchYAoq0k2t0F3UIXFLMJTMcetklrAvtnRC8MB5T2pRIewuS",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JP3qH7pIlOdM7I7AHHuXJv/PJUjYt57+VVTVCUQbK2OtT65I+OHCEQYUPF+5lKe6Gj6jEGjwUIDQpEHOpldYL+HT9Qu5Ksa1Mi4B0StVJgMmWTDzpsb8RCKmiElElgubpMe7W8LbtDg2DDbvFnI+yMd6IFrthsHljve3JpgRPVDovez4YoatsboTbZJenrxVWSRtpt4kIi5LipGUOIBjBkc",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JNW4VXidLGtYJ2pIr8AIr1HK/4E6dLWh+v3jwp9viSSgIp4czIhfhq4/qDZ2Gm6CJ6Rq6gpRH06Q1UzqbWnNhmZQDcuRriwG6uWK4qeH2TAFHnoPZeOHTeiic/PbT2gbBV2/A7eSeP5b258VoA6iLV+EHZJAJLE6ScRfX4uobMrnYT70491PRONkYqoGCGpPYZQCrGyE6tOfQkv/QsrVjUk",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JMsaN4XudiovuAMANtWQIw1WtkA994RmBGHSkxb6TXHStN3kigYRRhmpZ5mTg8c9e2YBJ68sAg6Wdf/oRJ/j4uBCkfkJv0/MnxvA/5xXcUegdAV3d1dTnltCkG5jhwYKoaPVG3o0reFufq0E8OEtcN5HjJ/+8HeZ5bAG/imIt+Af8B+6SQzIYBXpFff5/CdY9orGUFeFhv/gGGVI+1+jRQ5",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JMsaN4XudiovuAMANtWQIw10hPU/fSN0+rBQWQuNopgUksUoNqu/vNeQZGSOgbHuiMuqY8nojC5PvTW2QIF2X3/izPNrPoKbI4vIq6r8Rn+YipGMaPoaeNqFcDOxGXrLlfr9QQ95Ui334FjAkumW8zYX43g2AT6CcSOODr303ZNXYr9LY5CxY2QVeoEfovVG6Kw6WqiWOI2YyJgg9MtzRzB",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JPAXhrcpbSh0mEv+DbnRC1XwUZdWfS9y8w4pDHe+OtIdgpz4uJ4lyKNjX1IUW9K4Re/1oyBioGhoS+GmD6/IN+3syW8rJMRDOSw/ncrlKBInVkdlX8Zea5i5nj4+DrFFhhqYlKwyPqOkIY5NcYIh8E1vKjhspC4cU8ksU3i4yVXHjf28uZDkI9gHU9bQWMqRwOKZcaOIyc0g2hPBZKUEs1c",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JNuIr06U9N/vshMfMo1uMXbnXQBOHv+PYo9DAKTCx2rQ59TIzlPDNticWzwRHPSj6FcpLhnj1u6JJDEg6a7aexvtKG72tuTIjhUI1xaXBBiWq1EUoH8wUghQewEHvjtfqOZqD3vpug5Jmriaf4ye5JutmsWrF8U//6sryuGQJFr48sUY+2tb5PDNHAjwh1ooSxAJwT3cdWVhdkDCfE2xEFH",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JNuIr06U9N/vshMfMo1uMXbRM4bdrY54H+P7PayYcCRv5C4Rtam7xulNDZMhoV2Hw+QHn2HJJDhp8z5FHuRMFjzjbzWqEgxRTnmUfIN2IIhg9jtGLOqwLhlg2DIeF7zC0vuSYxrKwpkhLO4pDZg1eowDJAUbmGqRlFaMH5EmXF3n9gT84o5fX8NjT704r2M0qWZRXedolABsv47Hd5n5KGd",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JO3dL4Nss3tFsaOFyfNpX3GJSGmb/dDVAtSzqmcM+UeSyFUEVINwJnLn7n5URGLiBYAnl2cMg/m0DHQBNSo7tOW0wbqOqJlyWftm5CKjtVNNljjpFimlH0W0AGkYNdz+BjXNeiIvAxuYthA/8Llf8Mrpi50ehz21Md5SfaBb/QRVBMUNiLKXuTPzrNpk1EHMsq5UxTl84o+rLrWxB820e/B",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JOeOe8oPXvyvp8ZsxsnOtPbCfDQ3oFKoRkrTi+D3H3Geu/RTtgf2QfUYlAtMUkdft5M7k1MIoeSnth+x7jOuMvzdWRZhr4l1GzFyQ4rXOAyhsLPHPwi5AvcxFvPT/GDbDxigmSZxyWmK0KXbo+Tu2M6/SJKTs3nF2prmFaoXZrs1s1kx1OA9pce6eSIoovoHShGFcWyLU7ohzlt0XCvRD8e",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JOfHdOTExLvrCMVjmKJQ3YXIMG2CDSFQ+tYV3u0pf97AaetVpvT2M1GM6dovuIYcsgnBkv7ljmUdrESoa1UkYqi9pfSkErMZ9Fq1eeDlOVz/wQqferPuNEoIdRdVXXFM0n6QWwGea/rah3YbZj96NBt39d/VDC27BkS6+DEzGmsYeysXZ4fOONBnueq71/MGp5Zg37JStOVv+HwOzkUM+OS",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JMwoqJaXIX+yPLhSRg/cqPIjNJar3pKJxkN+4lekpoGut0T4q8GDybMzP9STP2BSYxq3iP4S+YpUCv7G0nPxnrm/OD9S0G2uphRUZrH18sWoq2eVVfljhVp8bjuFPx7MCPF/bAKDD28+rVAK42PBr9T5PctTuJKjFnz0wtnXNX71F2aIjZWOSSW37ZtU2O5Vs1LNQust3tUKGHVO+sR4cu6",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JMwoqJaXIX+yPLhSRg/cqPI+p/GLxxB4YQOzTylEiNCOKsLcVswtdvGZ8XAMfiQZbUDhBKtvyyfZxgHmdNeSlKKuM7hgkRK2YGtZxbJIT1Y9MTyd16cckET141EIXDa7FMzJB+b9yk++0FTH0gvIpM8kNJDpm3NKy7UQW/hsBfBKj8JEmwPCImhykElot5VX0gXRY3nnUGynQ6Hs3G+q12d",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JO0EEk9pJfnPWp1YD8psoMOD0hTSDlay51/H0hA2owsNE7ym6S+fInU3Q+FJMIeaffWfVmg869fRbE+xDupnHVuzz3D6vX2TnsjLJr0q62e707ht/Er2ZZcR5gQ+AxK6xQucQC4RS9dN7EfdRKy5fw29Z0EHuKTYM3ZQI7LFUHV7QwRKYu8VUubuf1v3VPmjFL5lHeaV/fk3i+5ixXXFiiH",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JO0EEk9pJfnPWp1YD8psoMOZwY0HQi0gHWLNWXEMKR5qm3sAYD9XOkXMrd3b6PhyfAdOKA6tQVknxm83gK3cSI53Hkyja/8n65pS1OXJlQWsHQmEJx0F1XJH9HOoLrfRsn6uMwtIllY75VKNUP8g5LynmZeY1StcBi/ceo8HSsi1nXLUUWa8Pa0m12hHDaJCPWgVD5Z4VqKQnBBozObhHQM",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JO0EEk9pJfnPWp1YD8psoMO9veDisiTymVvHFFvf2/TFIFblL/YT+tTVvRDuvqaTkdzEgMC1p+wf6V0rMa1/v4a1kHrOcOb1jt+zMJkBmmBD1oZ08BV9ywfVQP0uBeJYpDZGE97bMgPzHs9/Qqe2Xx/kJIaQuy4Lg3eQDD5g+WDETCxkzA1OflRdzumHjVKim6tOKk7kWKUnYp+V/88EGPQ",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JNGBkyrY3ZSgSmWg3YV/ndVwSiKRDL2ykAPzLLAYI0O2+XtA5c1xAZjoucs+omztUzbSQLxbIiLEL00deu/FDoAFB+bxvqB5P9wHtu0OILLGHOy9wsDP6QQB0Tk3Fx1Bljm9tck0aKRm28T0F3Sz809io2aLxWCiEkjX5LBe8dtCkOoY77vOGY9KSQHpQbwFDcapfmcgDibgU4zqjnJeky2",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JNGBkyrY3ZSgSmWg3YV/ndV9Mo57pwIcwYyydIIJYcFtsprJZ74t+35+wgzItSrnVCFmgwX04mpeH7CpltP++iEFKR0UYiXqma+apn41jODOEhS30Wx5AjPWJL4MFBnULXQme1V97nvRd/vvwoPfWBdGPGFXgLypZy9aTYkeRA5G9BgTZOCseEXhIm9YTrwkMsQjDTPGeDfXlgBwF+LiHei",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JPhjB5cHk8/f2U+7oOTqL0m+pstUDN556VlQkMLOkFZjb+cS6uIFbVO1bY/BHaHVnsonVQnKrzXCvjGz7BUFlDH9TmxMF9f82KZ/1wpJecImzUSis7PPjBCrABljH9T9bORRiXhS2eixxHrFr0FTLTwpcLm1+r93XsZ1DpUeDKVvnTfIaLVa0xLFZfpeZhdPU1gcmulYo4AJXALi6jwCE81",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JMvhqJRq9oujuj2aKBHDmZYTC3bySsTsl9NaaaV1W8wTi09Kmp4uUY8ZRkogmrsTFhuHjV/d02jD8rjgraOeQpCvHg/yn99EmRai+UUVHbwfnrGgFu4ci7Q5oao8+KgejXX+IbQv3K/nGJvX7FNKMH97peMa0LCbmBjskn/QL7AmcK3PNPxGYAOdZrCz2FzX/e8sYCK4J0POkmcMecitkm6",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JPS4+SU5gNagdl3q79JFMchezrM5aF3IiP5az3Vn2ZGO+SP6QUSvkpqc1PGTasaevx9HLNkGlSqhNFNLIB+1W4PdDiYKhr+NpWClzMaGUehstTbH65uE5efds/6d/cdxUXeJ2jjp5mck1b6mn51c/Z238/KvI6s2dNkYTMUcUC7SzFdbV+H+4OHa0Dkc7eQzoXSakskvKlmN3R/tfJUW3fj",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JPS4+SU5gNagdl3q79JFMch7lafzAwoxWcao+pgY+vbvktSbUIjfEXsPVSgEnBSG3QMZbGuu141M5OEDzyguWbAjUH75ziw9+Bvus9Q/SEwl3pC18SMgj94b6HYqn92p9BkhuzE/ltvlIbaijGMwkZKuwksC1j4ulh8Lo/R3qRZtqdgrFOk/YFSQSrsu7TRof0d09d35Tnvtr8pNwPJgAf0",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JPtjYyWprgd3/CAVMnZC4ParC/DbUme1Bhy7MOrOv+/06fKhSBxwVhoBtfXRYEDWHMQwpo9aId513qc2qnfy1hJsYkJmUxb/u3YrJU00TDSxsnxJ7qUOS478sgqTlIzGFb0szhVCRaA73qJBJGmxVXLxCLSe5pYnb/GYMDr0QRubl4tiHPYSNJNSfHK0G5Ectjd50phoNIjD2a539Au+Vrz",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JPtjYyWprgd3/CAVMnZC4PaND8juUyTUTLa61atann1rFBOHAk8TCK923MF/bXwJNyjH6zYrBQNu4oFcmKrZUZG2VJUTxsESJArl9/wGD+ZZzT0uyp7gVbePxXCE75r7jy3kgg1hBTHCps5dhl4poTmlHG+GxIBIma+OEd8IIIZsrBFbLQLdlG/vj4suUwiPhW08IBlDLYZ9dhn9TX4h7wN",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JPtjYyWprgd3/CAVMnZC4PaWwtrnG50AjB2Ft3vcoZyQXz/eWX6rr8lIjauGWJYpl7j6nCPwwIqKkTa9lHP49zTUHjNhhsCfN1heyGiRu/UVP5RDcJZIU3WAa5w9tgy06lHma1kaBrvEYJG0cKzB1t2vWox9pwz8zROvuLM8DRLUvNKZt2opkd7higEVqoTIgv0XAxHAGtyVyxalwlcnTLD",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JPtjYyWprgd3/CAVMnZC4PasPCa9WXjwTlQ+B5SbG/KAl8pOWwI2nq/WwcZTacuMzCGcYzU65sl/E9WPeoevXEccxMcZgLzIodMZRnskdz9MGo716DOIZ79hl12S9ScOcE8nF8l4KFFjRGvGBoUv452aJsAfFZLLvXyYCWjG6UkT4w2CjTiKPG+tp9CuzVEGuYl0mYsgb2alQxKwHv8menI",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JNJyA8NoZUiJkFdw53976uVURTxaxDq7av1sJm6ipRVLl6Ujrhc/0NPzlvrr0INhYvPfdFIvMqIxxh/N1RjTUbtf5lLng1dsay4+Ox7VfxzmTPKbrZokTHW7CV5H88a2tr7odUJZjzOEDS9YllaA5Re/Sp+I2wpQOXYYs5S/1CLcILHqg6bBaYU/0s44b2dOJmCWBtGnRyQTLnm8E+gHtnM",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JMGRzoYG6Fx+zXNWvLCxhQRgoLFjP//YxutG09jLOj13OJjTmvhUzJoCwNO2r3kUgEpTJPaw1IZH9Q9LYnHA12JB4290R/iwq8Sl++rUCvEyCQVDTX0cKZbBdObTa6VcngbO/Zot/9VyB8d+rtT42/zAp6gLaPDvPBztDRDZsLdIl6xKAFWVbqNp3LrpXAisWywFoUlkYfZqFhwGUoHFKEk",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JNpGqffFSGFc+ROPjPT/ZwAakBt2S6KmP67xFsAymaepcbaFrv1idt93KKTQclo3nnv5DI5qNSJG3nwFwehTz28brvY8ioYq5VIMZvr7hLr6e6xScwx55mnliROevPdBE39RPrkkwki8jWf4aSm0pgvFEhPlZB5YG5ymdPyVYuEUKfdgn1KfJvA+wAZGsFtwHwvdF9JihcQ9nzv3RNbmOLQ",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JNg/9V0yYaZb6WTiwlBPYyHZz9OeFrWU7OTDgYtNNGBQybzjRI/hzJqvr54IQ8sT6POjiVLj12LqlxWwVPZcEg/hOD2ZH5lO8vLZiclwC5k9Ug/IIoMrGaqUDKRTpnC04YQW5ObKMu3A3mHF5IlmZOghxwan9hMeKgBrzg6cJuT2Ajaz/wPuBSCwLRFrkJqv7uZ6QabyM9qOHrA2sctv5vA",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JOMKpmduVbKn34bAoyX+Si5nbhRubUXAY56RIcAbxY9FDeKrDGlecUEQ5btwAF9mFjjGFDS606lTsUcDYPD0UfJeFf3JUlLswPIfkPtX8fZdq6an735ANKYQXh6zm+sdAIoATZl8HdAjl6D/id4csOlAx4jAgGe80KRs8HQE+klQLrCu3rwYPS5DfyR3oyY7tLRAPXhxkbJbLfW0Rj1xY6j",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JO5rjQt0BvWA6dsabp+44AWwHmhBj1mK/kp+5g3+GGWVtaUr6J+LmlHsad0HNfR710m8fEwgNlnJmqIYu6JHF97q78AIpvEV8PterZL9R/DIbGgRrb4399i9y/n9JYYgikXQWlQqUFFcZqap69D+a1Md5VToVqVf4JVPFKdHBk/115uzJ/9wgALAEI+l1j8gkdtJ3ksBue9yU/FRrxTuDX8",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JPcJJC2IIy4u0VKrAIKFVLNO4adv6RG/ke0uy+BmM2cXqLFN9G1nVTQYqzxrWS7kVtrOa7f6rsG33bMIrtfEoJag8clL6dElz2bH3qJ/26xH0fMbpqQ/eBaYzFwu6UfH+7Xb3VK4ZLNfOUd6qHBSk5sabCJBqoZ2X/ZWkVI6w7QSUv36yci7T7z+L6jb9RpOW2jkwdHknHMWqugRXH78Krf",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JPcJJC2IIy4u0VKrAIKFVLNt31YGX1nn8U5lXIT+04DzaoIryKYTZh3IIAsI7633NG5QcFlovklTvymJbzADu9TzkTacVPlgIY3w47cBjA5PrDxnNkzDdGJYT9x+rSklvnazkb5qngybGNiMQPHxAy/94IhSdxtcAoI/4M7GfXYxIZjPyCN8MCjUi7l6llMBWi5QKe0ppN1MWAHtzFQMF/y",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JOY1rOHWfN3QDB63c8mqCpSwUloQGNaOL4lyoHMXuRnrUst5Ls3l3kc/3RbI0EZOzZmcBHD1W/iJnddSMiI2aX35nmZEXtJtcWJcwX/ONcTTCYxnhJF93McgeOQPIqMG+XTfWhtQgr4W5kqR/qJKi8+uHuAss57TqP2vSoj2TaPyPURr4TOe36r0pvFeki/cl8F47QHt8R7YJ9vhq3o+xOo",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JPl+HfINpcNsrCPSRUOuA4mDjZga5ErQDTv9AY9iHliy6DnWAcdjOA5XEe/to/lOOkO4k1zDAKeEPWJ0JNaFekc+8ySj7sAo1hyr26qFXTIEZBmSSq3CGga5p8q8b1NvY/u7l+jS5Ma7rrRd+7xCSRrT1xOV/Za5b7z2V4HeppAaJuoPEuy06tRJ5x5aENX410DLTBl/eUQUhnY8Qgu9ySD",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JPl+HfINpcNsrCPSRUOuA4mKflNOHQMcL8snUeiSWXUeeaq9S6Vhd4dACEyxKm9VaTbsRI+XHTGTjl4YvGlRX+8gTSvrZzDrAlAN5cN3a0Dfs9B+KbhwKUWl2C3RDNh1z5TjsVRAHsqBnlw/zYnIFNgyh6RY38YUjoywwD3Drq5JwC+F4B7giugg1d753RajTAD8IV8meRmgNwiNUmmM16N",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JNm9LFUkvSehxWFK65eMxj6vXkJ1XBlIxLRJCA26euwVyfvmNp6CTJBveYlgLNvre7X6xDGkgks4V3ZnNb52CU1EqhdaG3crK+hnjYXHqlwya/2KWgMDMQ5K+EyjNj65jNSk4LOTFzkortGQO/EDs8kHHT74nUQQ/xVg1janB25jTbRHFXCgOvzuaegRCCW0RmDGb84TQzCx4hyATNcAT0f",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JO4hrlulr+Hg5WUSq1xRwERr7nkrK7XX8IvK4gGJgaRJKgrAozXNDbgU+erGtSJd462sz2Ks4McJGxjjN8/PJjSbe8CZBzAG4F7oRZ02UEuWWVclVdH/iWvH1zdAIYBHCL2cXuKix4V1SapmoAVm2XRa1SG8iFcGTQgQVdeuBxRXT0nWOJ0qXlVBy8jdz9FmkWdDx5hNNjCJyjWf0d+82dc",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JOn5z04EWrEBFe67+jJkPQW2TyLFMBFHjgciz1QyIJMHAhu995nOnqD5eiZlmLoPNRwCXsg6eOiQy9VFuUAstmaGvQABluqrhEtDCVhUGC3kskP7MKoMW2zoU0m+3l6iLBMhvS2VO6/6A6s1xhPysrWfEHiXguIIrunk2tqFtKKTd7LmGErNazIWCdBn94MDq1lOKPTsPLT9+G5lEEIzh2T",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JOKXY6zFRD7uIw0Y82fnd9L3voDjKblfTsxIatgeZrLGuqmZZ2xDBajkwLCHrITE6bZygWudf7YWyeHkV+haWTu2OcCngmkkBZ9c2TfE7VfcaKE+2RJybYg+0EVqzNk+7HCkv0y9ONELqjCkJ+WZr1dirP6d5OPyIwCTRa8STR0Pjwq3yGdcKmkEZmFsF3wKaNcnxbNSCwqRDNkHoSDW7Uw",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JOxJiAFhGr/JeaCWenAVIuokJrOlqXBORJHo90NPdstfR3cOblVVaHPHMx/Ci01l7p+PiAyZCkeEuPnGjlipH1yvWAPdf6cEpuBEhtMq3Q0VR+5CQoGFaxTdOMfvRayyy3tkgJYwrZCCjSyBzlUwSCdzhMwxHUCFF/PNxGvILO2VIN1pqaDbH8mMYi1kU/lPbwubHv2pnCgCxo5Pt39o5HP",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JPAcRlj3o4JwXWVfcA5vXrmbRrJMt7cVehoOqoJAZM6j3avKT9LGxOux9KnMvmaQ6WOlFZtbCT1XJs3hv9hn62h5DXC17onEKuW8ehKOQCx2g+QLpW39gLmDCOW1Mr7dnrhyZIDkyptyY4aJ361B9/0awOb0jkSf9gAVSdaOiGHJjQVr+aYcy+JGHPf8X3wvL8xPA3TF1oyGLn3/J8tQLZP",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JOUrvbR2tio/2i5tFBvr/g88n+7L4TNb/6W/M9RO9ZBO7ApM1loPzaGbIEpim4m6bcnLOtN9EXxENG4qlGKxq3ugtiRG8PvXWdcXGkyMTN6lCT/w9ebLzKHVo9O3iJcdUl5c1tCNNMFZmfCORA6DVar47DA73sVIwmZS3QwrXWYnc4gB2eQJz2AoBS7/VCgCAbEUZqu//MOofyE/dwWCpUE",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JMIaWgHSHU3Z3D5N1jqrg465oAi+tj3T9rutnCe9slplEEUAzgjDxO8N2gOlgzJEw31eagD992G5ue+5sMZeQewY7PyTi8QTYCESa9KOVVB4GrQx9DPNGuvjcPLSplB03D512nVgeh4BYN35mb3Cuiiu/ks1/jxb8yUCsiSvwRvkMxMa4ZRrlcHpj3TJvglBModYomZfzhjaPGPJzwI0dVm",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JPPmzHewzJ6RbWHEKgxZY3gFo4XHAjn2Y4kizaaNKJVhz3wyhgWnGVziCMCOuz51WS/oNqrfBlMDc/0Gk+EfZl5CjYQFyAJbKTXZDlTPKZZyijtuQQxw4GlLTTNM6t5waeRZlZbqycX1jRtG7EJeRU6Feh0UF05oJrfu72+wiszJEMRvqGi7+WArAcMjDDn6+s69l5WEje2wxqVbFUkwJEf",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JPPmzHewzJ6RbWHEKgxZY3gpgQ7hni7vOydoKULBiCc271Q8xvUoSoE3zv/XH6n1hpKdUJprc+64MQ2K5zp9+3Y37Te0jAGVQPkFJLzeUgcZCekrWY55WRQrcJ3jU6YWFh+ce93voA6Qt1XozyD5zkVgQpT0f/jyg6QjwNVWadpie7YrDIfyyS3evYWTeCjoU9PUVtXG1GDAGa+9nyoFsCR",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JNuI2spmUu6ExU/wMgEclKperPFdkYMI4WJaIQMqN/HqX0ABRSlc4COlwU5rsUDETa4Wju1fYhxn+gzZ3SGvXtZ5Otm4CK+XyKTOaW0bBfdB5KfKuQrLIPOnI1oAcO7Wq80klYDAtRvzc4Xp7KLjhAVf3Yv3vbcXKaib8VXCd0hU5a93AfQUi93vAtEKr8K2+svMa7ENsEEIXU+8tAGpUL+",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JP+xXhU099C6IwXBIOO7M9FoD/xsM3cvlPSusXcTvMgLZFK6gVRL1m+TzOUUReg4dQefeUW5s6GvkW9ESBH3SAbSEVeJT+0jQhioAvRreVn+PUzRb10KBoLZnRECUD1nlYGHrSHV2dqJwZkJ8Juj4HoohK2zXIjksPZiuw2ovBHMq3rH0ACWS8JRyybIAscslRtvmJgxoagtMB89ll7He4q",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JMKvzq8TMJ6GwZlHoAmwd1kHrdv9WYMOtRXYwQ5Yy8qbuQLdgla1+25L+U5vKXoLquDqISOC3vveNQfZuLG6VO+IuandygocUkVxAYdnTYv4lm2g1PgXaL+diRlS649URMV4MqOQrQHxV8WQV/yEYdDSjZMuuCUcYe+mpvBM9IUZ0c5dyskTJEvGribKO3E9FC1fO/yK7qimJTXAeGIDRmC",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JMKvzq8TMJ6GwZlHoAmwd1kw8Ndja5FmcLCrDIWDA1mucJGJYhn93xmBpKQAQOelx9wgoBy0fOdRgJ5bpbFf+uLUHip9xpVgT7jU+DMWTOnq4QEmYn+RcI8meECuhS2YgoHgSN+G4IB7lAlNoe6alhpcigIOgczCrfotArb22xP88H8MDRFBYSpUy6ZCUMfCY1k+TrH6eJmfEvBzPaL3qVv",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JOT816XuZOvw4eUMoXLToe7Y6TCOnY7dhjAWNskJkYYiPAUiDTUYMfaKvVfcegoBwXb4uZZrc/KuLrGNHPR/zZsvRuLd7K5JPz7PK7OdUPDwzg+vAms66j2BJ7xmEbCSCajtCAK0q8csQKjq5slzvbq+CyaDvNlX+xq50qjI0dEuaVdi/ZlT+mh5dwaHA/iMTqLZF2rE/io0WsF0iNbXXoE",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JPE3p+DzHTFK7G1yYJTtzRNR7xoW5hHq3SEq3a0p8no7n2lTgrTYtxvRXPZc50cRK05HNRmWYGz5jnsOr3LQJNJZ8cTYwJvMNgnLf5FQY2kCUD9CpnNTYWdM7RjOCsqek9GwnTIvgJ3U6BqoLv268+i8+SCDVD0D9aFVuYTMWWndfkIcVYrpxqCEyTcovcxeh1nUah9avuqydjD2H0O/TW/",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JPE3p+DzHTFK7G1yYJTtzRNwHdnBY0jTUQXGT+YmE+eLMraxw2QZAZxdJUnZ0VXr8YHzlMMyleD51SlCFESuX+MGSlqS6KS4+4QHM2RBzGxULXsP5F8mDER51w7aovyugXjvfROEojcaST8r0/1KGD7lBSvjOJP0J66niV9+UNE7yZDyWoxneXoptWEpaHF08MBmMH3sXPKOAmcFK4N7mMn",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JNwX00yVFr9+bTGYhL8XtzVj6CdRDV+9An+pU8/99KYzhtgv03J8ZN4opDstIL5ghDYgndqFRudRRvscBi2QBq8kCysay1P5TBet7P8DFw8LAadom9lNxAGCSKpSoEAdkYbZGjc+R8/g2MHdpRwcTFPlsk5p4Z/UJgwXYRPkMTd3Rq3FgDNntEk9zwgO8lk8AeB1j6j8R7TmTAhUA5jx61B",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JNwX00yVFr9+bTGYhL8XtzVmiaZCnYYQezX52Ix+wy1Nm8QWqEJao8M7d9ijlYhaYpBWsvmhtPhoJV0uvpKZpT/3slD4uh+U6DiauWrWir0RY2rL6hgZK18Lkkn8oeCiG5AYuD2uJj19hWjIEkMBpuzz4PvaQ8W/cs4m1tLHGTfmeMpKz0aIFzGsCyWbBo9xH0WhwZ+TyvvAn1zy8AhX2sk",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JNtOilJYCXkLOurnLZPlYJOfCw/F32CDShIzqJLb35GFLPaBqNazKIbtK2ZWkiXgNH8YJYmKUis0Hkhw05AJesgpk6LAE5HRq9vMBXMclHJ3HeIwNRqNMxg/vuHPcncZH0GerTN+sWWry69IgnpFckY/P8NFdNnu7M+goZrmblLoIc7udfc9knU/726wRuyo6tFhnpnn5TS8qCDj0UmA/LF",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JNtOilJYCXkLOurnLZPlYJOOraGHyne0SnvdEGc4QZhoS2fk0m3O/MFsEprrZcaTwWeDovf8VhAPJEZuKgoI1C4PM1H+bft6r/kz9DFacDFqEzF1pOG+oOfYQc4cNjD4rk1MA8/VUoy/d167CGfYV53CgBhu3oZWh00Ns4NBk9C7GEI8yqJ5lD4XwQxG0Cn/Ny4U44iqC2DrsB70iE9hBvh",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JNqMiq9wZi3+Pql0LBpdEZzOcR9RZsTMYTfx3IoQK/mVW4jyBvzMD6vQuz86Bz5ke2v3U4o8f9pyKOw3FD9xb7Wy0ktMMOW/qIWNGXTEbpq25D78IC8GBQg5q3jOgqvsn3rYLI+nmfBTyFD+KFsaRsKO8bMgIb/ZfuFmoiC2YKkfGLmNOJtldkvc0pw/t2uMSC4W4WBEY0X1JOIFNS3LS5y",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JNqMiq9wZi3+Pql0LBpdEZz8PgXwRd2lqZRH2jFM/vtbn+WrUK0MLZOst/WKeyr1WIsB0hnr4MQYWIMFN4ipXyjZh8ow7slXGY1bc8SS3jujBAvk9HoDPmY2cKQQoJKmo4hrC/kZSKEQ5jTwuCWYuXOqgpwVDtGZBxjCpfANB/I3zovO2pLSLXypKxaO07FsSfU+x2fBqNxRA3ElC1j+vHw",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JOx4cz7yty/Y+vm4rYo+r5XSu08w9G2B8LODBye5xM/CSSlPPVOhx/nFZ14ndpyfOr5SF08Bhxu2NHvdgjSxBuo4looX/15FDgv9kkaxtbR600BODAVuFmTyD/u0cjjEK5qIKidjtfieV7IvdwPg1ud5TvFEdILnqtdK8L4YpL6l8tZEFQZgU5i8d4kc4TFXq/TVDtgkQ1fx9kSy1V1ud6s",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JOx4cz7yty/Y+vm4rYo+r5Xy9c31n/R7W1EftIP/7hsUPtHvVWzEO3X0hDW8601Z103IM6KQro8rDkyhICozlA960iMXKxNAHRMvw+rkPe9b/Vlfkj7ORyWrBf7ScZ6rtSI8u+G8EEKDr5TSJw4Yi1Me5nJeyDd0amCVx67KxO5KKszH1epxGHKdlRDQBIIXvARqzKbHIrpAJHzuOs67iBr",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JO7F8iJJP36CE5jt4MZQydCpeLlSAx64nWiLlJrIRpBbE8hitGDtjS7ucs8nhvFXsxZJ2UazryaL8C/08YgNnPMLB4LP8JDHqNDpssyVsdGZ1e8oy+LW4BQ6cnkoMxCw5bkHmvuuGlJeJS6r2CmwD3HEllj4gOCMFiUvTS/fztUJ7ag0kHqA7Z8qLXhTHa/+lq5Dk7Qppw0leDbDQBguzXN",
                "T2zHw2Zb3mr1mvNqEj+Krl2gujEf/+A3yCVJL4880JPm2IwZfBb7ITVPDq31b1zK1ArlmAgs2KuJyh4Ah6a/jt+l0gDUKND6yE5qrAMFAdksKff1jZJYK0xHqMpU5Pa6pPvnB+O6PK/j/oVgfgSDsNkiKH0AHgnhzvyu8KhSsaD6UEpLKPBOkLh9e0FX+65TFUvVPCjoCAINoNT4hYyUcpIzNt33AIu/A+8yuiwcBQ8ILJD3TUAHiKbgRr+O4rvm"};

        AES aes = new AES(Mode.CBC, Padding.PKCS5Padding, Base64.decode(key), Base64.decode(iv));

        for (int i = 0; i < 500; i++) {
            new Thread(() -> {
                for (int j = 0; j < aa.length; j++) {
                    String mingwen = aes.decryptStr(aa[j]);
                    System.out.println("解密数据 " + mingwen);
                    String miwen = Base64.encode(aes.encrypt(mingwen));
                    System.out.println("加密数据 "+ miwen);
                }
            }).start();
        }*/

        // for (int j = 0; j < aa.length; j++) {
        //     LOGGER.info("解密数据 = {}", new String(decrypt(aa[j], key, iv)));
        // }
    }
}

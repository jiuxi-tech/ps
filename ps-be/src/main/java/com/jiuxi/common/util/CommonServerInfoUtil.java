package com.jiuxi.common.util;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.IdUtil;
import com.jiuxi.common.exception.ExceptionUtils;
import com.jiuxi.common.server.bean.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import oshi.util.Util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Properties;

/**
 * @ClassName: SystemInfoUtil
 * @Description: 系统信息工具类
 * <pre>
 *     使用时，需引入一下依赖：
 *      <dependency>
 *             <groupId>net.java.dev.jna</groupId>
 *             <artifactId>jna</artifactId>
 *             <version>5.14.0</version>
 *     </dependency>
 *     <dependency>
 *         <groupId>net.java.dev.jna</groupId>
 *         <artifactId>jna-platform</artifactId>
 *         <version>5.14.0</version>
 *     </dependency>
 * </pre>
 * @Author 杨占锐
 * @Date 2024/3/12 9:10
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
public class CommonServerInfoUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonServerInfoUtil.class);

    /**
     * 生成服务器信息
     *
     * @param serverInfo ServerInfo本身 或 继承自ServerInfo
     * @return com.jiuxi.common.bean.ServerInfo
     * @author 杨占锐
     * @date 2024/3/12 9:15
     */
    public static <T extends ServerInfo> void buildServerInfo(T serverInfo) {

        try {

            setServerInfo(serverInfo);
        } catch (Throwable e){
            LOGGER.warn("生成服务器信息失败，e:{}", ExceptionUtils.getStackTrace(e));
        }
    }

    /**
     * 获取服务器信息
     *
     * @return com.jiuxi.common.bean.ServerInfo
     * @author 杨占锐
     * @date 2024/3/12 13:53
     */
    private static <T extends ServerInfo> void setServerInfo(T serverInfo) {

        SystemInfo si = new SystemInfo();

        HardwareAbstractionLayer hal = si.getHardware();

        try {
            // 设置CPU信息
            setCpuInfo(hal.getProcessor(), serverInfo.getCpu());
        } catch (Throwable e) {
            LOGGER.warn("设置CPU信息失败，e:{}", ExceptionUtils.getStackTrace(e));
        }
        // 设置内存信息
        setMemInfo(hal.getMemory(), serverInfo.getMem());
        // 设置服务器信息
        setSysInfo(serverInfo.getSys());
        // 设置Java虚拟机
        setJvmInfo(serverInfo.getJvm());
        // 设置磁盘信息
        setSysFiles(si.getOperatingSystem(), serverInfo.getSysFiles());

        serverInfo.setMsgKey(IdUtil.simpleUUID());
        serverInfo.setCreateTime(CommonDateUtil.now());
    }

    /**
     * 设置CPU信息
     */
    private static void setCpuInfo(CentralProcessor processor, Cpu cpu) {
        // CPU信息
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        Util.sleep(1000);
        long[] ticks = processor.getSystemCpuLoadTicks();
        long nice = ticks[1] - prevTicks[1];
        long irq = ticks[5] - prevTicks[5];
        long softirq = ticks[6] - prevTicks[6];
        long steal = ticks[7] - prevTicks[7];
        long cSys = ticks[2] - prevTicks[2];
        long user = ticks[0] - prevTicks[0];
        long iowait = ticks[4] - prevTicks[4];
        long idle = ticks[3] - prevTicks[3];
        long totalCpu = user + nice + cSys + idle + iowait + irq + softirq + steal;
        cpu.setCpuNum(processor.getLogicalProcessorCount());
        cpu.setTotal(CommonArithmeticUtil.round(CommonArithmeticUtil.mul(totalCpu, 100), 2));
        cpu.setSys(CommonArithmeticUtil.round(CommonArithmeticUtil.mul(CommonArithmeticUtil.div(cSys, totalCpu, 4), 100), 2));
        cpu.setUsed(CommonArithmeticUtil.round(CommonArithmeticUtil.mul(CommonArithmeticUtil.div(user, totalCpu, 4), 100), 2));
        cpu.setWait(CommonArithmeticUtil.round(CommonArithmeticUtil.mul(CommonArithmeticUtil.div(iowait, totalCpu, 4), 100), 2));
        cpu.setFree(CommonArithmeticUtil.round(CommonArithmeticUtil.mul(CommonArithmeticUtil.div(idle, totalCpu, 4), 100), 2));
    }

    /**
     * 设置内存信息
     */
    private static void setMemInfo(GlobalMemory memory, Memory mem) {
        mem.setTotal(CommonArithmeticUtil.div(memory.getTotal(), (1024 * 1024 * 1024), 2));
        mem.setUsed(CommonArithmeticUtil.div(memory.getTotal() - memory.getAvailable(), (1024 * 1024 * 1024), 2));
        mem.setFree(CommonArithmeticUtil.div(memory.getAvailable(), (1024 * 1024 * 1024), 2));
    }

    /**
     * 设置服务器信息
     */
    private static void setSysInfo(Sys sys) {
        Properties props = System.getProperties();
        sys.setComputerName(getHostName());
        sys.setComputerIp(getHostIp());
        sys.setOsName(props.getProperty("os.name"));
        sys.setOsArch(props.getProperty("os.arch"));
        sys.setUserDir(props.getProperty("user.dir"));
    }


    /**
     * 获取IP地址
     *
     * @return 本地IP地址
     */
    private static String getHostIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
        }
        return "127.0.0.1";
    }

    /**
     * 获取主机名
     *
     * @return 本地主机名
     */
    private static String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
        }
        return "未知";
    }

    /**
     * 设置Java虚拟机
     */
    private static void setJvmInfo(Jvm jvm) {
        Properties props = System.getProperties();
        jvm.setTotal(CommonArithmeticUtil.div(Runtime.getRuntime().totalMemory(), (1024 * 1024), 2));
        jvm.setMax(CommonArithmeticUtil.div(Runtime.getRuntime().maxMemory(), (1024 * 1024), 2));
        jvm.setFree(CommonArithmeticUtil.div(Runtime.getRuntime().freeMemory(), (1024 * 1024), 2));
        jvm.setVersion(props.getProperty("java.version"));
        jvm.setHome(props.getProperty("java.home"));
    }

    /**
     * 设置磁盘信息
     */
    private static void setSysFiles(OperatingSystem os, List<SysFile> sysFiles) {
        FileSystem fileSystem = os.getFileSystem();
        List<OSFileStore> fsArray = fileSystem.getFileStores();
        for (OSFileStore fs : fsArray) {
            long free = fs.getUsableSpace();
            long total = fs.getTotalSpace();
            long used = total - free;
            SysFile sysFile = new SysFile();
            sysFile.setDirName(fs.getMount());
            sysFile.setSysTypeName(fs.getType());
            sysFile.setTypeName(fs.getName());
            sysFile.setTotal(convertFileSize(total));
            sysFile.setFree(convertFileSize(free));
            sysFile.setUsed(convertFileSize(used));
            sysFile.setUsage(CommonArithmeticUtil.mul(CommonArithmeticUtil.div(used, total, 4), 100));
            sysFiles.add(sysFile);
        }
    }

    /**
     * 字节转换
     *
     * @param size 字节大小
     * @return 转换后值
     */
    private static String convertFileSize(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;
        if (size >= gb) {
            return String.format("%.1f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        } else {
            return String.format("%d B", size);
        }
    }
}

package com.cloud.auto.code.util;

/**
 * @author liulei
 * 包路径工具
 */
public class PackageUtil {

    public static final String filePathSeparator = "/";

    public static final String packSeparator = ".";

    /**
     * 包路径校验
     * 去除连续多个..情况 如 com..ls
     *
     * @param pack 包路径  com.ccc.cc.ccc
     * @return
     */
    public static String checkPack(String pack) {
        String[] packs = pack.split("\\.");
        return clearPack(check(packs, packSeparator));
    }

    /**
     * 包路径合并 用.最佳
     *
     * @param packs
     * @return
     */
    public static String mergePack(String... packs) {
        return checkPack(clearPack(merge(packSeparator, packs)));
    }


    /**
     * 清除包末尾的小数点.
     *
     * @param pack
     * @return
     */
    public static String clearPack(String pack) {
        return clear(pack, packSeparator);
    }

    /**
     * com.tat.ctt  转换位  com/tat/ctt/
     * 包路径转换问文件路径
     *
     * @param pack
     * @return
     */
    public static String packToFilePath(String pack) {
        pack = checkPack(pack);
        String[] packs = pack.split("\\.");
        StringBuilder stringBuffer = new StringBuilder();
        for (String p : packs) {
            if (p.length() > 1) {
                stringBuffer.append(p).append(filePathSeparator);
            }
        }
        return stringBuffer.toString();
    }


    /**
     * 包路径校验
     * 去除连续多个//情况 如 com//ls
     *
     * @param pack 包路径  com/ccc/cc/ccc
     * @return
     */
    public static String checkFilePath(String pack) {
        String[] packs = pack.split(filePathSeparator);
        return clearFilePath(check(packs, filePathSeparator));
    }

    /**
     * 文件路径合并
     *
     * @param packs
     * @return
     */
    public static String mergeFilePath(String... packs) {
        return checkFilePath(clearFilePath(merge(filePathSeparator, packs)));
    }


    /**
     * 清除文件末尾的小数点.
     *
     * @param pack
     * @return
     */
    public static String clearFilePath(String pack) {
        return clear(pack, filePathSeparator);
    }


    /**
     * 文件径校验
     * 去除连续多个 symbol字段 情况 如 com..ls  更改位com.ls
     *
     * @param packs  拆分后的路径字符串  com ccc cc ccc
     * @param symbol
     * @return
     */
    private static String check(String[] packs, String symbol) {
        StringBuilder stringBuffer = new StringBuilder();
        for (String p : packs) {
            if (p.length() > 1) {
                stringBuffer.append(p).append(symbol);
            }
        }
        return stringBuffer.toString();
    }

    /**
     * 包路径合并 用symbol合并追加
     *
     * @param symbol
     * @param packs
     * @return
     */
    private static String merge(String symbol, String... packs) {
        StringBuilder stringBuffer = new StringBuilder();
        for (String pack : packs) {
            if (pack != null && !pack.trim().isEmpty()) {
                stringBuffer.append(pack).append(symbol);
            }
        }
        return stringBuffer.toString();
    }


    /**
     * 清除包末尾的指定字符串.
     *
     * @param pack
     * @param symbol
     * @return
     */
    private static String clear(String pack, String symbol) {
        if (pack != null && pack.length() > 0) {
            String st = pack.substring(pack.length() - 1);
            if (symbol.equals(st)) {
                return pack.substring(0, pack.length() - 1);
            }
        }
        return pack;
    }

    public static void main(String[] args) {
        System.out.println(mergeFilePath("com/66//", "com/88/", "cccc/899//"));
        System.out.println(packToFilePath(mergePack("com", "com", "cccc")));
    }
}

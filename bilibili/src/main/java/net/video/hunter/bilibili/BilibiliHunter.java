package net.video.hunter.bilibili;

import org.jetbrains.annotations.NotNull;

/**
 * @author chpengzh
 */
public interface BilibiliHunter {
    static BilibiliHunter getDefault() {return new BilibiliHunterImpl();}

    Comment huntComment(@NotNull Sort sort, long oid, int page);

    Bilibili huntAV(long aid);
}

package com.huwenkai.com.mobilesafe.db.Domian;

import android.graphics.drawable.Drawable;

/**
 * Created by huwenkai on 2016/5/15.
 */
public class ProcessInfo {
    public String name;//应用名称
    public Drawable icon;//应用图标
    public long memSize;//应用已使用的内存数
    public boolean isCheck;//是否被选中
    public boolean isSystem;//是否为系统应用
    public String packageName;//如果进程没有名称,则将其所在应用的包名最为名称

    /**
     * Returns a string containing a concise, human-readable description of this
     * object. Subclasses are encouraged to override this method and provide an
     * implementation that takes into account the object's type and data. The
     * default implementation is equivalent to the following expression:
     * <pre>
     *   getClass().getName() + '@' + Integer.toHexString(hashCode())</pre>
     * <p>See <a href="{@docRoot}reference/java/lang/Object.html#writing_toString">Writing a useful
     * {@code toString} method</a>
     * if you intend implementing your own {@code toString} method.
     *
     * @return a printable representation of this object.
     */
    @Override
    public String toString() {
        return "ProcessInfo{" +
                "name='" + name + '\'' +
                ", icon=" + icon +
                ", memSize=" + memSize +
                ", isCheck=" + isCheck +
                ", isSystem=" + isSystem +
                ", packageName='" + packageName + '\'' +
                '}';
    }
}

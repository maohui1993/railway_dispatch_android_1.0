package cn.dazhou.commonlib.bean;

/**
 * Created by Hooyee on 2017/11/16.
 */

public class FileBean {
    /** @param data 记录上传的文件的分片总数  String：文件名  Integer：当前片数
     *             Map数据格式：
            *             <p>
     *             Map<String, Object> param = new HashMap<>();
     *             param.put("thumbnail", filename);
     *             param.put("remark", "暂无备注");
     *             param.put("functions", "路径： " + result.getPath());
     *             param.put("size", sizestr);
     *             param.put("length", length);
     *             param.put("filename", filename);
     *             param.put("filepath", filepath);
     *             param.put("chuck", 0);
     *             param.put("flag", null);
     *             param.put("finishchuck", "");
     *             param.put("isNew", true);
     */

    private String thumbnail;
    private String remark;
    private String functions;
    private String size;
    private long length;
    private String filepath;
    private int chuck;
    private boolean flag;
    private String finishchuck;
    private boolean isNes;
    private String filename;
    private float process;

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getFunctions() {
        return functions;
    }

    public void setFunctions(String functions) {
        this.functions = functions;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public int getChuck() {
        return chuck;
    }

    public void setChuck(int chuck) {
        this.chuck = chuck;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getFinishchuck() {
        return finishchuck;
    }

    public void setFinishchuck(String finishchuck) {
        this.finishchuck = finishchuck;
    }

    public boolean isNes() {
        return isNes;
    }

    public void setNes(boolean nes) {
        isNes = nes;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public float getProcess() {
        return process;
    }

    public void setProcess(float process) {
        this.process = process;
    }

    @Override
    public boolean equals(Object obj) {
        FileBean bean;
        if (obj instanceof  FileBean) {
            bean = (FileBean) obj;
            if (filename.equals(bean.filename)
                    && filepath.equals(bean.filepath)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }

    }
}

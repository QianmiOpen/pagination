package com.demo.app.util;

/**
 * 通用分页
 *
 * @author @mysoko
 */
public class Pagination {

    private int items_per_page = 10;         // 每页显示的实体数量
    private int num_display_entries = 10;    // 显示分页链接数
    private int current_page = 0;            // 当前页数
    private int num_edge_entries = 0;        // 开始和结尾几个数字
    private String link_to = "#";            // 链接地址
    private String prev_text = "Prev";       // 上一页，显示的文字
    private String next_text = "Next";       // 下一页，显示的文字
    private String ellipse_text = "...";     // 省略号，显示的文字
    private boolean prev_show_always = true; // 上一页，是否要显示
    private boolean next_show_always = true; // 下一页，是否要显示

    private Integer maxentries; // 总共记录数

    public void setItems_per_page(int items_per_page) {
        this.items_per_page = items_per_page;
    }

    public void setNum_display_entries(int num_display_entries) {
        this.num_display_entries = num_display_entries;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public void setNum_edge_entries(int num_edge_entries) {
        this.num_edge_entries = num_edge_entries;
    }

    public void setLink_to(String link_to) {
        this.link_to = link_to;
    }

    public void setPrev_text(String prev_text) {
        this.prev_text = prev_text;
    }

    public void setNext_text(String next_text) {
        this.next_text = next_text;
    }

    public void setEllipse_text(String ellipse_text) {
        this.ellipse_text = ellipse_text;
    }

    public void setPrev_show_always(boolean prev_show_always) {
        this.prev_show_always = prev_show_always;
    }

    public void setNext_show_always(boolean next_show_always) {
        this.next_show_always = next_show_always;
    }

    public void setMaxentries(Integer maxentries) {
        this.maxentries = (maxentries == null || maxentries < 0) ? 1 : maxentries;
    }

    /**
     * 计算最大页数
     */
    public int numPages() {
        return (int) Math.ceil((double)maxentries / (double)items_per_page);
    }

    /**
     * 计算开始和结束的分页链接，取决于
     * current_page 和 num_display_entries
     */
    public int[] getInterval() {
        int ne_half = (int) Math.ceil((double)num_display_entries / 2d);
        int np = numPages();
        int upper_limit = np - num_display_entries;
        int start = current_page > ne_half ? Math.max(Math.min(current_page - ne_half, upper_limit), 0) : 0;
        int end = current_page > ne_half ? Math.min(current_page + ne_half, np) : Math.min(num_display_entries, np);
        return new int[]{start, end};
    }

    public String drawLinks() {
        StringBuffer panel = new StringBuffer("");
        int[] interval = getInterval();
        int np = numPages();

        // Generate "Previous"-Link
        if (!prev_text.equals("") && (current_page > 0 || prev_show_always)) {
            appendItem(current_page - 1, new Appendopts(prev_text, "prev"), np, panel);
        }

        // Generate starting points
        if (interval[0] > 0 && num_edge_entries > 0) {
            int end = Math.min(num_edge_entries, interval[0]);
            for (int i = 0; i < end; i++) {
                appendItem(i, null, np, panel);
            }

            if (num_edge_entries < interval[0] && !ellipse_text.equals("")) {
                panel.append(new HTMLTag(HTMLTag.Tag.SPAN, ellipse_text));
            }

        }

        // Generate interval links
        for (int i = interval[0]; i < interval[1]; i++) {
            appendItem(i, null, np, panel);
        }

        if (interval[1] < np && num_edge_entries > 0) {
            if (np - num_edge_entries > interval[1] && !ellipse_text.equals("")) {
                panel.append(new HTMLTag(HTMLTag.Tag.SPAN, ellipse_text));
            }

            int begin = Math.max(np - num_edge_entries, interval[1]);
            for (int i = begin; i < np; i++) {
                appendItem(i, null, np, panel);
            }

        }

        // Generate "Next"-Link
        if (!next_text.equals("") && (current_page < np - 1 || next_show_always)) {
            appendItem(current_page + 1, new Appendopts(next_text, "next"), np, panel);
        }

        return panel.toString();
    }

    // Helper function for generating a single link (or a span tag if it's the current page)
    private void appendItem(int page_id, Appendopts appendopts, int np, StringBuffer panel) {
        page_id = page_id < 0 ? 0 : (page_id < np ? page_id : np - 1);
        if (appendopts == null) {
            appendopts = new Appendopts();
            appendopts.text = String.valueOf(page_id + 1);
            appendopts.classes = "";
        }

        HTMLTag lnk = new HTMLTag(HTMLTag.Tag.SPAN, appendopts.text).addAttribute(HTMLTag.Attribute.CLASS, "current");
        if (page_id != current_page) {
            lnk = new HTMLTag(HTMLTag.Tag.A, appendopts.text).addAttribute(HTMLTag.Attribute.HREF, link_to.replace("__id__", String.valueOf(page_id)));
        }

        if (!"".equals(appendopts.classes)) {
            lnk.addAttribute(HTMLTag.Attribute.CLASS, appendopts.classes);
        }

        panel.append(lnk);
    }

    private class Appendopts {
        public String text;
        public String classes;

        public Appendopts() {
        }

        public Appendopts(String text, String classes) {
            this.text = text;
            this.classes = classes;
        }
    }

    public static void main(String[] args) {
        Pagination p = new Pagination();
        p.setMaxentries(1000);
        p.setCurrent_page(10);
        p.setItems_per_page(5);
        p.setNum_display_entries(5);
        p.setNum_edge_entries(1);
        System.out.println(p.drawLinks());
    }

}

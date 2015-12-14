/**
 *实验室项目的工具方法
 */

/**
 *关闭所有弹出层
 */
function closeLayerAll() {
    layer.closeAll();
}

/**
 *    根据Url弹出层
 */
function layerDialog(url, title, width) {
    width = width || "90%";
    closeLayerAll();
    dialog = $.layer({
        type: 1,
        title: title,
        fix: false,
        border: "0px",
        offset: ["-100px", ""],
        area: [width, 'auto'],
        page: {
            url: url
        },
    });

}
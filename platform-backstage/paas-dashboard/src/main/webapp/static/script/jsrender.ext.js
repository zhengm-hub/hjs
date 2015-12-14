$.views.helpers({
    cutStr: function (str) {
        if (str != null) {
            if (str.length >= 20) {
                return str.substring(0, 20) + "...";
            } else {
                return str;
            }
        } else {
            return "";
        }
    },
    turn: function (str) {
        //         var r=new RegExp("fsda","gm");
        // str.replace(r,"tihuan");

        if (str != null) {
            str = str.replace(/&lt;/g, "<");
            str = str.replace(/&gt;/g, ">");

            return $(str)[0].innerHTML;
        } else {
            return "";
        }
    }
});
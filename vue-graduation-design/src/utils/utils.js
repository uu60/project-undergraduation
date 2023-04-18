export default {
    sortChange(column, tableData) {
        if (column.order == "ascending") {
            tableData.sort((a, b) => {
                switch (column.prop) {
                    case "createTime":
                    case "doneTime":
                        return new Date(a[column.prop]) - new Date(b[column.prop]);
                    case "conf":
                        return a[column.prop] - b[column.prop];
                    default:
                        console.log(b[column.prop].localeCompare(a[column.prop]));
                        return a[column.prop].localeCompare(b[column.prop]);
                }
            });
        } else if (column.order == "descending") {
            tableData.sort((a, b) => {
                switch (column.prop) {
                    case "createTime":
                    case "doneTime":
                        return new Date(b[column.prop]) - new Date(a[column.prop]);
                    case "conf":
                        return b[column.prop] - a[column.prop];
                    default:
                        console.log(b[column.prop].localeCompare(a[column.prop]));
                        return b[column.prop].localeCompare(a[column.prop]);
                }
            });
        }
    },
    getData(vue, url) {
        vue.$axios
            .get(vue.$store.state.backendAddress + url)
            .then((res) => {
                if (res.data.code == 200) {
                    vue.tableData = res.data.tasks;
                    let date = new Date();
                    let hours = date.getHours();
                    let minutes = date.getMinutes();
                    let seconds = date.getSeconds();
                    vue.lastUpdateTime =
                        date.getFullYear() +
                        "-" +
                        date.getMonth() +
                        "-" +
                        date.getDate() +
                        " " +
                        (hours < 10 ? "0" + hours : hours) +
                        ":" +
                        (minutes < 10 ? "0" + minutes : minutes) +
                        ":" +
                        (seconds < 10 ? "0" + seconds : seconds);
                } else if (res.data.code != 412) {
                    vue.$message.error(vue.$store.state.serverErrMsg);
                }
            })
            .catch((err) => {
                console.log(err);
                vue.$message.error(vue.$store.state.serverErrMsg);
            });
    },
}
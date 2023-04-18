<template>
  <div>
    <el-container>
      <el-header>
        <el-row style="height: 100%">
          <el-col
            :span="12"
            style="height: 100%; display: flex; align-items: center"
          >
            <h1 style="font-size: 20px; margin: 0px">
              <span style="color: #409eff">正在处理</span>的任务
            </h1>
          </el-col>
          <el-col
            :span="12"
            style="
              display: flex;
              align-items: center;
              justify-content: right;
              height: 100%;
            "
          >
            <span style="font-size: 10px; color: grey; margin-right: 10px"
              >上次刷新时间：{{ lastUpdateTime }}</span
            >
            <el-button
              type="primary"
              icon="el-icon-refresh"
              circle
              @click="getData"
              style="float: right"
            ></el-button>
          </el-col>
        </el-row>
      </el-header>
      <el-main>
        <el-table
          :data="
            tableData.slice(
              (currentPage - 1) * pageSize,
              currentPage * pageSize
            )
          "
          stripe
          @sort-change="sortChange"
        >
          <el-table-column prop="name" label="任务名称" sortable="custom">
          </el-table-column>
          <el-table-column prop="model" label="选用模型" sortable="custom">
          </el-table-column>
          <el-table-column prop="conf" label="置信度" sortable="custom">
          </el-table-column>
          <el-table-column prop="createTime" label="创建时间" sortable="custom">
          </el-table-column>
          <el-table-column prop="state" label="状态" sortable="custom">
            <template slot-scope="scope">
              <el-tag
                :type="scope.row.state == '正在分析' ? 'primary' : 'success'"
                disable-transitions
                >{{ scope.row.state }}</el-tag
              >
            </template>
          </el-table-column>
          <el-table-column prop="comment" label="任务注释" sortable="custom">
          </el-table-column>
        </el-table>
        <el-pagination
          background
          layout="total, sizes, prev, pager, next, jumper"
          :total="tableData.length"
          :current-page="currentPage"
          :page-size="pageSize"
          :page-sizes="[5, 10, 15, 20]"
          align="center"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        >
        </el-pagination>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import utils from "../utils/utils.js"
export default {
  data() {
    return {
      tableData: [],
      currentPage: 1,
      pageSize: 10,
      lastUpdateTime: "",
    };
  },
  methods: {
    //每页条数改变时触发 选择一页显示多少行
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`);
      this.currentPage = 1;
      this.pageSize = val;
    },
    //当前页改变时触发 跳转其他页
    handleCurrentChange(val) {
      this.currentPage = val;
    },
    sortChange(column) {
      utils.sortChange(column, this.tableData);
    },
    getData() {
      utils.getData(this, "/task/processing_task");
    },
  },
  mounted() {
    this.getData();
  },
};
</script>

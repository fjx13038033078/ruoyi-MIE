<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="订单号" prop="orderNo">
        <el-input
          v-model="queryParams.orderNo"
          placeholder="请输入订单号"
          clearable
          style="width: 200px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="收货人" prop="receiverName">
        <el-input
          v-model="queryParams.receiverName"
          placeholder="请输入收货人"
          clearable
          style="width: 150px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="手机号" prop="receiverPhone">
        <el-input
          v-model="queryParams.receiverPhone"
          placeholder="请输入手机号"
          clearable
          style="width: 150px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="支付状态" prop="payStatus">
        <el-select v-model="queryParams.payStatus" placeholder="请选择" clearable style="width: 120px">
          <el-option label="未支付" value="0" />
          <el-option label="已支付" value="1" />
        </el-select>
      </el-form-item>
      <el-form-item label="订单状态" prop="orderStatus">
        <el-select v-model="queryParams.orderStatus" placeholder="请选择" clearable style="width: 120px">
          <el-option label="待发货" value="0" />
          <el-option label="已发货" value="1" />
          <el-option label="已完成" value="2" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['mall:order:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['mall:order:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="orderList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="订单号" align="center" prop="orderNo" width="180">
        <template slot-scope="scope">
          <span class="order-no">{{ scope.row.orderNo }}</span>
        </template>
      </el-table-column>
      <el-table-column label="用户" align="center" prop="userName" width="100" />
      <el-table-column label="收货人" align="center" prop="receiverName" width="100" />
      <el-table-column label="收货电话" align="center" prop="receiverPhone" width="120" />
      <el-table-column label="收货地址" align="center" prop="receiverAddress" :show-overflow-tooltip="true" />
      <el-table-column label="总金额" align="center" prop="totalAmount" width="100">
        <template slot-scope="scope">
          <span class="price">¥{{ scope.row.totalAmount }}</span>
        </template>
      </el-table-column>
      <el-table-column label="支付状态" align="center" prop="payStatus" width="90">
        <template slot-scope="scope">
          <el-tag :type="scope.row.payStatus === '1' ? 'success' : 'warning'" size="small">
            {{ scope.row.payStatus === '1' ? '已支付' : '未支付' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="订单状态" align="center" prop="orderStatus" width="90">
        <template slot-scope="scope">
          <el-tag :type="getOrderStatusType(scope.row.orderStatus)" size="small">
            {{ getOrderStatusText(scope.row.orderStatus) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="下单时间" align="center" prop="createTime" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="150">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleView(scope.row)"
            v-hasPermi="['mall:order:query']"
          >详情</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['mall:order:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['mall:order:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 订单详情对话框 -->
    <el-dialog title="订单详情" :visible.sync="detailOpen" width="600px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="订单号" :span="2">
          <span class="order-no">{{ detailForm.orderNo }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="用户">{{ detailForm.userName }}</el-descriptions-item>
        <el-descriptions-item label="下单时间">{{ parseTime(detailForm.createTime) }}</el-descriptions-item>
        <el-descriptions-item label="收货人">{{ detailForm.receiverName }}</el-descriptions-item>
        <el-descriptions-item label="收货电话">{{ detailForm.receiverPhone }}</el-descriptions-item>
        <el-descriptions-item label="收货地址" :span="2">{{ detailForm.receiverAddress }}</el-descriptions-item>
        <el-descriptions-item label="订单金额">
          <span class="price">¥{{ detailForm.totalAmount }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="支付状态">
          <el-tag :type="detailForm.payStatus === '1' ? 'success' : 'warning'" size="small">
            {{ detailForm.payStatus === '1' ? '已支付' : '未支付' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="订单状态" :span="2">
          <el-tag :type="getOrderStatusType(detailForm.orderStatus)" size="small">
            {{ getOrderStatusText(detailForm.orderStatus) }}
          </el-tag>
        </el-descriptions-item>
      </el-descriptions>
      <div slot="footer" class="dialog-footer">
        <el-button @click="detailOpen = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 修改订单状态对话框 -->
    <el-dialog title="修改订单" :visible.sync="updateOpen" width="500px" append-to-body>
      <el-form ref="updateForm" :model="updateForm" label-width="100px">
        <el-form-item label="订单号">
          <span class="order-no">{{ updateForm.orderNo }}</span>
        </el-form-item>
        <el-form-item label="支付状态">
          <el-radio-group v-model="updateForm.payStatus">
            <el-radio label="0">未支付</el-radio>
            <el-radio label="1">已支付</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="订单状态">
          <el-radio-group v-model="updateForm.orderStatus">
            <el-radio label="0">待发货</el-radio>
            <el-radio label="1">已发货</el-radio>
            <el-radio label="2">已完成</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="收货人">
          <el-input v-model="updateForm.receiverName" placeholder="请输入收货人" />
        </el-form-item>
        <el-form-item label="收货电话">
          <el-input v-model="updateForm.receiverPhone" placeholder="请输入收货电话" />
        </el-form-item>
        <el-form-item label="收货地址">
          <el-input v-model="updateForm.receiverAddress" type="textarea" :rows="2" placeholder="请输入收货地址" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitUpdate">确 定</el-button>
        <el-button @click="updateOpen = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listOrder, getOrder, updateOrder, delOrder } from "@/api/mall/order";

export default {
  name: "Order",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 订单表格数据
      orderList: [],
      // 详情弹出层
      detailOpen: false,
      // 详情表单
      detailForm: {},
      // 修改弹出层
      updateOpen: false,
      // 修改表单
      updateForm: {},
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        orderNo: undefined,
        receiverName: undefined,
        receiverPhone: undefined,
        payStatus: undefined,
        orderStatus: undefined
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询订单列表 */
    getList() {
      this.loading = true;
      listOrder(this.queryParams).then(response => {
        this.orderList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 获取订单状态文本 */
    getOrderStatusText(status) {
      const map = {
        '0': '待发货',
        '1': '已发货',
        '2': '已完成'
      };
      return map[status] || '未知';
    },
    /** 获取订单状态标签类型 */
    getOrderStatusType(status) {
      const map = {
        '0': 'warning',
        '1': 'primary',
        '2': 'success'
      };
      return map[status] || 'info';
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.orderId)
      this.multiple = !selection.length
    },
    /** 查看详情按钮操作 */
    handleView(row) {
      this.detailForm = { ...row };
      this.detailOpen = true;
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      getOrder(row.orderId).then(response => {
        this.updateForm = response.data;
        this.updateOpen = true;
      });
    },
    /** 提交修改 */
    submitUpdate() {
      updateOrder(this.updateForm).then(response => {
        this.$modal.msgSuccess("修改成功");
        this.updateOpen = false;
        this.getList();
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const orderIds = row.orderId || this.ids;
      this.$modal.confirm('是否确认删除订单编号为"' + orderIds + '"的数据项？').then(function() {
        return delOrder(orderIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('mall/order/export', {
        ...this.queryParams
      }, `order_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>

<style scoped lang="scss">
.order-no {
  font-family: monospace;
  color: #409EFF;
  font-weight: 500;
}

.price {
  color: #f56c6c;
  font-weight: 600;
}
</style>

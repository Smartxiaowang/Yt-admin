<template>
  <div class="app-container">
    <el-tabs v-model="activeTab" type="border-card" @tab-click="handleClick">
      <el-tab-pane v-for="role in roleList" :name="role.role_ID" :key="role.role_ID" :label="role.role_NAME">
        <el-table
          v-loading="listLoading"
          :data="roleList_z"
          :header-cell-style="{
            'font-weight': 'bold',
            'color': '#000',
            'padding':'14px 0'
          }"
          :row-style="{'height':'54px'}"
          border
          fit
          highlight-current-row>
          <el-table-column type="index" label="序号" fixed width="50" align="center"/>
          <el-table-column prop="role_NAME" label="角色" fixed width="200" show-overflow-tooltip/>
          <el-table-column v-for="item in buttonlist" :key="item.FHBUTTON_ID" :label="item.NAME" :render-header="labelHead" align="center">
            <template slot-scope="{row}">
              <template v-for="(varRb,vsRb) in roleFhbuttonlist">
                <template v-if="row.role_ID == varRb.ROLE_ID && item.FHBUTTON_ID == varRb.BUTTON_ID">
                  <span :key="vsRb" style="display: none;">{{ switchModel[row.role_ID + '_' + item.FHBUTTON_ID] = true }}</span>
                </template>
              </template>
              <el-switch v-model="switchModel[row.role_ID + '_' + item.FHBUTTON_ID]" @change="upRb(row.role_ID,item.FHBUTTON_ID)"/>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import { requestFN } from '@/utils/request'
export default {
  data() {
    return {
      activeTab: '1',
      roleList: [],			// list 列出组(页面横向排列的一级组)
      roleList_z: [],			// list 列出此组下架角色
      buttonlist: [],			// list 列出所有按钮
      roleFhbuttonlist: [],	// list 列出所有角色按钮关联数据
      pd: [],					// map
      ROLE_ID: '1',			// 角色ID
      switchModel: {},
      edit: false,
      listLoading: false			// 加载状态
    }
  },
  created() {
    this.getList(this.ROLE_ID)
  },
  methods: {
    labelHead(h, { column, index }) {
      const l = column.label.length
      const f = 34 // 自定义文字宽度
      column.minWidth = f * l
      return h('div', { class: 'table-head', style: { width: '100%' }}, [column.label])
    },
    handleClick(tab) {
      this.ROLE_ID = tab.name
      this.roleList_z = []
      this.getList(this.ROLE_ID)
    },
    getList(ROLE_ID) {
      this.listLoading = true
      this.ROLE_ID = ROLE_ID
      requestFN(
        '/buttonrights/list',
        {
          ROLE_ID: ROLE_ID
        }
      ).then((data) => {
        this.roleList = data.roleList
        this.roleList_z = data.roleList_z
        this.buttonlist = data.buttonlist
        this.roleFhbuttonlist = data.roleFhbuttonlist
        this.pd = data.pd
        this.hasButton()
        this.listLoading = false
      }).catch((e) => {
        this.listLoading = false
      })
    },
    hasButton: function() {
      var keys = 'buttonrights:edit'
      requestFN(
        '/head/hasButton',
        {
          keys: keys
        }
      ).then((data) => {
        this.edit = data.buttonrightsfhadminedit
      }).catch((e) => {
        this.listLoading = false
      })
    },
    upRb: function(ROLE_ID, FHBUTTON_ID) {
      requestFN(
        '/buttonrights/upRb',
        {
          ROLE_ID: ROLE_ID,
          BUTTON_ID: FHBUTTON_ID
        }
      ).then((data) => {
        this.switchModel = {}
        this.roleFhbuttonlist = []
        this.getList(this.activeTab)
      }).catch((e) => {

      })
    }
  }
}
</script>


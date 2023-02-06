<template>
  <div>
    <div class="app-container mt-20" style="padding-right:90px">
      <el-form ref="form" :model="form" :form="form" :rules="rules" label-width="240px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="企业名称" prop="CORP_NAME">
              <el-input v-model="form.CORP_NAME" placeholder="请输入内容" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="统一社会信用代码" prop="CODE">
              <el-input v-model="form.CODE" placeholder="请输入内容" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <div class="form-group">
              <label class="form-label"><span class="star">*</span>所属区域</label>
              <div class="input-block">
                <el-form-item prop="PROVINCE" label-width="0px">
                  <el-select ref="PROVINCE" v-model="form.PROVINCE" placeholder="请选择" class="mr-20">
                    <el-option v-for="item in provinceList" :key="item.BIANMA" :label="item.NAME" :value="item.BIANMA" />
                  </el-select>
                </el-form-item>
                <el-form-item prop="CITY" label-width="0px">
                  <el-select ref="CITY" v-model="form.CITY" placeholder="请选择" class="mr-20">
                    <el-option v-for="item in cityList" :key="item.BIANMA" :label="item.NAME" :value="item.BIANMA" />
                  </el-select>
                </el-form-item>
                <el-form-item prop="COUNTRY" label-width="0px">
                  <el-select ref="COUNTRY" v-model="form.COUNTRY" placeholder="请选择">
                    <el-option v-for="item in countryList" :key="item.BIANMA" :label="item.NAME" :value="item.BIANMA" />
                  </el-select>
                </el-form-item>
              </div>
            </div>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="通讯地址" prop="ADDRESS">
              <el-input v-model="form.ADDRESS" placeholder="请输入内容" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮政编码">
              <el-input v-model="form.POSTAL_CODE" placeholder="请输入内容" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="成立时间">
              <el-date-picker id="CREATE_DATE" ref="CREATE_DATE" v-model="form.CREATE_DATE" value-format="yyyy-MM-dd" format="yyyy-MM-dd" style="width: 100%" type="date" placeholder="请选择" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="规模">
              <el-select v-model="form.SCALE" placeholder="请选择">
                <el-option v-for="item in scaleList" :key="item.DICTIONARIES_ID" :label="item.NAME" :value="item.DICTIONARIES_ID" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="经度">
              <el-input v-model="form.LONGITUDE" placeholder="请输入内容" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <div class="ud-flex">
              <div style="flex:1">
                <el-form-item label="纬度">
                  <el-input v-model="form.LATITUDE" placeholder="请输入内容" disabled />
                </el-form-item>
              </div>
              <div>
                <el-button style="margin-left:10px" @click="handleMap">
                  <span class="svg-container">
                    <svg-icon icon-class="international" />
                  </span>
                </el-button>
              </div>
            </div>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="职工人数(人)">
              <el-input v-model="form.EMPLOYEES" placeholder="请输入内容" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="占地面积（㎡）">
              <el-input v-model="form.AREA_COVERED" placeholder="请输入内容" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="资产总额（万元）">
              <el-input v-model="form.TOTALASSETS" placeholder="请输入内容" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="注册资金（万元）">
              <el-input v-model="form.REGCAPITAL" placeholder="请输入内容" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="上年度实际营收（万元）">
              <el-input v-model="form.SALESREVENUE" placeholder="请输入内容" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="年利润（万元）">
              <el-input v-model="form.ANNUALPROFIT" placeholder="请输入内容" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="经济类型">
              <SelectTree ref="jjlxTree" :clearable="false" :options="jjlxList" :props="defaultProps" v-model="form.ECO_TYPE" placeholder="请选择" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所属行业">
              <SelectTree ref="hylxTree" :clearable="false" :options="hylxList" :props="defaultProps" v-model="form.INDUSTRY" placeholder="请选择" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="法定代表人">
              <el-input v-model="form.LR_NAME" placeholder="请输入内容" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="法人职务">
              <el-input v-model="form.LR_POSITION" placeholder="请输入内容" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="法人办公电话">
              <el-input v-model="form.LR_PHONE" placeholder="请输入内容" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="法人手机号">
              <el-input v-model="form.LR_MOBILE" placeholder="请输入内容" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="主要负责人">
              <el-input v-model="form.CHECKPERSON" placeholder="请输入内容" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="主要负责人电话" prop="CHECKPERSONPHONE">
              <el-input v-model="form.CHECKPERSONPHONE" placeholder="请输入内容" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="安全负责人">
              <el-input v-model="form.CONTACTS" placeholder="请输入内容" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="安全负责人手机号">
              <el-input v-model="form.CONTACTS_PHONE" placeholder="请输入内容" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <div class="lh-1">
              <el-form-item label="营业执照">
                <div class="disContent">
                  <div v-for="(item, index) in bus_images" :key="index" class="img-div">
                    <img :src="config.fileUrl + item.FILEPATH" alt="">
                    <div class="disContent-hide">
                      <el-tooltip class="item" effect="dark" content="预览" placement="top">
                        <span class="el-icon-zoom-in yuLan">
                          <viewer :images="bus_images">
                            <img :src="config.fileUrl + item.FILEPATH" alt="" class="yuLanImg">
                          </viewer>
                        </span>
                      </el-tooltip>
                      <el-tooltip class="item" effect="dark" content="删除" placement="top">
                        <span class="Delete" @click="deleteImage(index)"><i class="el-icon-delete" /></span>
                      </el-tooltip>
                    </div>
                  </div>
                  <el-upload ref="upload" :on-preview="handlePictureCardPreview" :before-upload="beforeFileUpload" :auto-upload="false" class="avatar-uploader" action="#" accept=".jpg,.jpeg,.png" list-type="picture-card">
                    <i class="el-icon-plus" />
                  </el-upload>
                </div>

                <el-dialog :visible.sync="dialogVisible">
                  <img :src="dialogImageUrl" width="100%" alt="">
                </el-dialog>
              </el-form-item>
            </div>

          </el-col>
        </el-row>
      </el-form>
    </div>
    <div class="ui-height" />
    <div class="ui-foot">
      <el-button type="success" @click="confirm">保存</el-button>
      <el-button type="primary" @click="goBack">返回</el-button>

    </div>

    <el-dialog :visible.sync="dialogFormMap" title="编辑坐标" width="800px" class="dy-dialog">
      <baidu-map
        v-loading="loadingMap"
        :zoom="zoom"
        :scroll-wheel-zoom="true"
        :style="{height: clientHeight+'px'}"
        @click="getClickInfo"
        @ready="handler">
        <bm-view style="width: 100%; height:100%; flex: 1" />
        <bm-control ref="control" :offset="{width: 10, height: 10}" anchor="BMAP_ANCHOR_TOP_LEFT">
          <div class="map-flex">

            <div class="map-lable">关键词：</div>
            <div style="flex:1;">
              <el-autocomplete v-model="addressKeyword" :fetch-suggestions="querySearch" :trigger-on-focus="false" placeholder="请输入详细地址" style="width:100%" @select="handleSelect" />
            </div>
            <!--            <el-input v-model="addressKeyword" style="width: 200px" placeholder="请输入内容" />-->
            <!--            <el-button slot="append" icon="el-icon-search" @click="mapNameChange"/>-->
          </div>
        </bm-control>
        <!--        <bm-local-search :keyword="addressKeyword" :auto-viewport="true"/>-->
      </baidu-map>
      <div slot="footer" class="dialog-footer">
        <span>经度：</span>
        <el-input v-model="LATITUDE" style="width: 200px" placeholder="请输入内容" disabled />
        <span>纬度：</span>
        <el-input v-model="LONGITUDE" style="width: 200px" placeholder="请输入内容" disabled />
        <el-button @click="dialogFormMap = false">取 消</el-button>
        <el-button type="primary" @click="setPosition">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { requestFN } from '@/utils/request'
import SelectTree from '@/components/SelectTree'
import { upload } from '@/utils/upload'
export default {
  components: { SelectTree },
  data() {
    return {
      CORPINFO_ID: this.$parent.CORPINFO_ID,
      config: config,
      // 地图相关
      // clientHeight: document.documentElement.clientHeight - 250,
      clientHeight: 600,
      loadingMap: true,
      listLoading: false,
      BMap: '',
      map: '',
      showMap: false,
      addressKeyword: '',
      pointLngLat: '',
      zoom: 10,
      dialogFormMap: false,
      LATITUDE: '',
      LONGITUDE: '',
      dialogImageUrl: '',
      dialogVisible: false,
      provinceList: [],
      cityList: [],
      countryList: [],
      scaleList: [],
      hylxList: [],
      jjlxList: [],
      defaultProps: {
        children: 'nodes',
        label: 'name'
      },
      form: {},
      bus_images: [],
      bus_files: [],
      imgUrl: require('@/assets/images/map.png'),
      rules: {
        CORP_NAME: [
          { required: true, message: '企业名称不能为空', trigger: 'blur' }
        ],
        CODE: [
          { required: true, message: '统一社会信用代码不能为空', trigger: 'blur' }
        ],
        PROVINCE: [
          { required: true, message: '省不能为空', trigger: 'blur' }
        ],
        CITY: [
          { required: true, message: '市不能为空', trigger: 'blur' }
        ],
        COUNTRY: [
          { required: true, message: '区不能为空', trigger: 'blur' }
        ],
        ADDRESS: [
          { required: true, message: '通讯地址不能为空', trigger: 'blur' }
        ],
        CHECKPERSONPHONE: [
          { required: true, message: '主要负责人电话不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  watch: {
    'form.PROVINCE': {
      handler() {
        this.getCityList()
      }
    },
    'form.CITY': {
      handler() {
        this.getCountryList()
      }
    }
  },
  created() {
    this.getData()
    this.getHylx()
    this.getJjlx()
    this.getProvinceList()
    this.getDict()
  },
  methods: {
    handleMap() {
      this.dialogFormMap = true
      this.LATITUDE = this.form.LATITUDE
      this.LONGITUDE = this.form.LONGITUDE
    },
    getClickInfo(e) {
      this.LONGITUDE = e.point.lng
      this.LATITUDE = e.point.lat
    },
    setPosition() {
      this.dialogFormMap = false
      this.form.LATITUDE = this.LATITUDE
      this.form.LONGITUDE = this.LONGITUDE
    },
    getData() {
      requestFN(
        '/corpinfo/goEdit',
        {
          CORPINFO_ID: this.CORPINFO_ID,
          tm: new Date().getTime()
        }
      ).then((data) => {
        const PROVINCE = data.pd.PROVINCE + '000000'
        const CITY = data.pd.CITY + '000000'
        const COUNTRY = data.pd.COUNTRY + '000000'

        data.pd.PROVINCE = PROVINCE.substring(0, 6)
        data.pd.CITY = CITY.substring(0, 6)
        data.pd.COUNTRY = COUNTRY.substring(0, 6)

        this.form = Object.assign({}, data.pd)
        this.bus_images = data.busImgs
      }).catch((e) => {
        this.$message({
          message: '操作失败',
          type: 'error'
        })
      })
    },
    deleteImage(index) {
      this.$confirm('确定要删除吗?', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        console.info(index)
        requestFN(
          '/imgfiles/delete',
          {
            IMGFILES_ID: this.bus_images[index].IMGFILES_ID
          }
        ).then(() => {
          this.$message({
            message: '删除成功',
            type: 'success'
          })
          this.bus_images.splice(index, 1)
        }).catch((e) => {
        })
      }).catch(() => {
      })
    },
    confirm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          const loading = this.$loading({
            lock: true,
            text: '提交中...',
            spinner: 'el-icon-loading',
            background: 'rgba(0, 0, 0, 0.7)'
          })
          this.form.COMPANY_AREA = this.$refs.PROVINCE.selected.label + this.$refs.CITY.selected.label + this.$refs.COUNTRY.selected.label
          requestFN(
            '/corpinfo/edit',
            this.form
          ).then((data) => {
            if (this.$refs.upload.uploadFiles.length > 0) {
              loading.close()
              this.uploadImg()
            } else {
              loading.close()
              this.$message({
                message: '操作成功',
                type: 'success'
              })
              this.$parent.activeName = 'CorpList'
            }
          }).catch((e) => {
            loading.close()
            this.$message({
              message: '操作失败',
              type: 'error'
            })
          })
        }
      })
    },
    uploadImg() {
      this.$refs.upload.submit()
      const loading = this.$loading({
        lock: true,
        text: '上传中...',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      })
      const formData = new FormData()
      for (var i = 0; i < this.bus_files.length; i++) {
        if (this.bus_files[i]) {
          formData.append('FFILE', this.bus_files[i])
        }
      }
      formData.append('FOREIGN_KEY', this.form.CORPINFO_ID)
      formData.append('TYPE', 1)
      upload(
        '/imgfiles/add',
        formData
      ).then((data) => {
        loading.close()
        this.$message({
          message: '操作成功',
          type: 'success'
        })
        this.$parent.activeName = 'CorpView'
      }).catch((e) => {
        loading.close()
        this.$message({
          message: '营业执照上传失败',
          type: 'error'
        })
      })
    },
    beforeFileUpload(file) {
      const types = ['image/jpeg', 'image/jpg', 'image/png']
      const isImage = types.includes(file.type)
      if (!isImage) {
        this.$message.error('上传图片只能是 JPG、JPEG、PNG 格式!')
      } else {
        this.bus_files.push(file)
      }
    },
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url
      this.dialogVisible = true
    },
    getHylx() {
      requestFN(
        '/dictionaries/listHylxDictToCreateCode',
        {}
      ).then((data) => {
        this.hylxList = JSON.parse(data.zTreeNodes)
      }).catch((e) => {
        this.listLoading = false
      })
    },
    getJjlx() {
      requestFN(
        '/dictionaries/listJjlxDictToCreateCode',
        {}
      ).then((data) => {
        this.jjlxList = JSON.parse(data.zTreeNodes)
      }).catch((e) => {
        this.listLoading = false
      })
    },
    getProvinceList() {
      requestFN(
        '/dictionaries/getLevels',
        {
          DICTIONARIES_ID: 'e725d2a91b8248f4b8f49889038df7de'
        }
      ).then((data) => {
        /* this.provinceList = data.list*/
        this.provinceList = [{ DICTIONARIES_ID: 'acd05c42faf346db9a0d067402b97c10', BIANMA: '130000', NAME: '河北省' }]
      }).catch((e) => {
      })
    },
    async getCityList() {
      var dictID = await this.getDictID(this.form.PROVINCE)
      requestFN(
        '/dictionaries/getLevels',
        {
          DICTIONARIES_ID: dictID
        }
      ).then((data) => {
        this.cityList = data.list
      }).catch((e) => {
      })
    },
    async getCountryList() {
      var dictID = await this.getDictID(this.form.CITY)
      requestFN(
        '/dictionaries/getLevels',
        {
          DICTIONARIES_ID: dictID
        }
      ).then((data) => {
        this.countryList = data.list
      }).catch((e) => {
      })
    },
    getDictID(BIANMA) {
      return new Promise((resolve) => {
        requestFN(
          '/dictionaries/findByBianma',
          {
            BIANMA: BIANMA
          }
        ).then((data) => {
          resolve(data.DICTIONARIES_ID)
        }).catch((e) => {
        })
      })
    },
    getDict() {
      requestFN(
        '/dictionaries/getLevels',
        {
          DICTIONARIES_ID: '37b045e160c04ddba851073b4e510cc9'
        }
      ).then((data) => {
        this.scaleList = data.list
      }).catch((e) => {
      })
    },
    // 地图相关
    mapOpen() {
      this.top = this.getScrollTop()
      if (this.top) {
        this.setScrollTop(0)
      }
    },
    // 关闭地图后调用
    mapClose() {
      this.setScrollTop(this.top)
      this.top = 0
      this.showMap = false
    },
    getScrollTop() {
      let scrollTop = 0
      if (document.documentElement && document.documentElement.scrollTop) {
        scrollTop = document.documentElement.scrollTop
      } else if (document.body) {
        scrollTop = document.body.scrollTop
      }
      return scrollTop
    },
    setScrollTop(top) {
      if (!isNaN(top)) {
        if (document.documentElement && document.documentElement.scrollTop !== undefined) {
          document.documentElement.scrollTop = top
        } else if (document.body) {
          document.body.scrollTop = top
        }
      }
    },
    // 地图初始化
    handler({ BMap, map }) {
      this.mapOpen()
      this.BMap = BMap
      this.map = map
      this.loadingMap = true
      var geolocation = new BMap.Geolocation()
      const myGeo = new BMap.Geocoder()
      var $this = this
      // 调用百度地图api 中的获取当前位置接口
      geolocation.getCurrentPosition(function(r) {
        myGeo.getLocation(new BMap.Point(r.point.lng, r.point.lat), function(result) {
          if (result) {
            $this.loadingMap = false
            $this.$set($this, 'pointLngLat', { lng: result.point.lng, lat: result.point.lat })
            map.enableScrollWheelZoom(true) // 开启鼠标滚轮缩放,默认关闭
            $this.setCenter({ BMap, map })
          }
        })
      })
    },
    // 搜索地图
    querySearch(queryString, cb) {
      var options = {
        onSearchComplete: function(results) {
          if (local.getStatus() === 0) {
            // 判断状态是否正确
            var s = []
            for (var i = 0; i < results.getCurrentNumPois(); i++) {
              var x = results.getPoi(i)
              var item = { value: x.address + x.title, point: x.point }
              s.push(item)
              cb(s)
            }
          } else {
            cb()
          }
        }
      }
      var local = new this.BMap.LocalSearch(this.map, options)
      local.search(queryString)
    },
    handleSelect(item) {
      var { point } = item
      this.map.clearOverlays() // 清除地图上所有覆盖物
      this.map.centerAndZoom(point, this.zoom)
      const marker = new this.BMap.Marker(point) // 创建标注
      this.map.addOverlay(marker) // 将标注添加到地图中
      marker.enableDragging() // 可拖拽
      this.LONGITUDE = point.lng
      this.LATITUDE = point.lat
    },
    // 设置打开中心位置
    setCenter({ BMap, map }) {
      var point = new BMap.Point(this.form.LONGITUDE, this.form.LATITUDE)
      const zoom = map.getZoom()
      setTimeout(() => {
        map.centerAndZoom(point, zoom)
      }, 0)
      // var marker = new BMap.Marker(point) // 创建标注
      // map.addOverlay(marker) // 将标注添加到地图中
    },
    goBack() {
      this.$parent.activeName = 'CorpList'
    }
  }
}
</script>

<style lang="sass" scoped>
.avatar-uploader
  ul
    margin: 0
    li
      padding: 0
.el-dialog__body
  padding: 0
.map-flex
  display: flex
  width: 500px
  align-items: center

.ud-flex
  display: flex
.form-group
  display: flex
  align-items: center
  .form-label
    padding: 0px 15px
    font-size: 14px
    width: 240px
    font-weight: 400
    text-align: right
    margin-bottom: 18px
    .star
      color: red
      padding-right: 4px
  .input-block
    display: flex

.disContent
  display: flex
  align-items: center
  flex-wrap: wrap
  .img-div
    position: relative
    margin: 0 10px 0px 0
    width: 108px
    height: 108px
    border-radius: 4px
    overflow: hidden
    &>img
      width: 100%
      height: 100%

.disContent-hide
  position: absolute
  width: 100%
  height: 100%
  border-radius: 4px
  background-color: rgba(48, 48, 48, 0.59)
  display: none
  top: 0
  left: 0
  line-height: 32px

  .Delete
    position: absolute
    bottom: 14px
    right: 10px
    font-size: 16px
    color: white
    cursor: pointer
  .yuLan
    position: absolute
    bottom: 23px
    right: 50px
    font-size: 16px
    color: white
    cursor: pointer
    .yuLanImg
      position: absolute
      bottom: 0
      right: 0
      width: 100%
      height: 100%
      opacity: 0

.img-div:hover .disContent-hide
  display: block

  .pitchCss
    border: 1px solid #202e78
    transition: all linear 0.1s
    width: 116px
    height: 116px

</style>


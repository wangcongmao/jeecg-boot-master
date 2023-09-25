<template>
  <div>
    <BasicForm @register="registerForm" ref="formRef"/>
    <!-- 子表单区域 -->
    <a-tabs v-model:activeKey="activeKey" animated  @change="handleChangeTabs">
          <a-tab-pane tab="订单商品" key="cesOrderGoods" :forceRender="true">
            <JVxeTable
              keep-source
              resizable
              ref="cesOrderGoods"
              v-if="cesOrderGoodsTable.show"
              :loading="cesOrderGoodsTable.loading"
              :columns="cesOrderGoodsTable.columns"
              :dataSource="cesOrderGoodsTable.dataSource"
              :height="340"
              :rowNumber="true"
              :rowSelection="true"
              :disabled="formDisabled"
              :toolbar="true"
            />
          </a-tab-pane>
          <a-tab-pane tab="订单客户" key="cesOrderCustomer" :forceRender="true">
            <JVxeTable
              keep-source
              resizable
              ref="cesOrderCustomer"
              v-if="cesOrderCustomerTable.show"
              :loading="cesOrderCustomerTable.loading"
              :columns="cesOrderCustomerTable.columns"
              :dataSource="cesOrderCustomerTable.dataSource"
              :height="340"
              :rowNumber="true"
              :rowSelection="true"
              :disabled="formDisabled"
              :toolbar="true"
            />
          </a-tab-pane>
    </a-tabs>

    <div style="width: 100%;text-align: center" v-if="!formDisabled">
      <a-button @click="handleSubmit" pre-icon="ant-design:check" type="primary">提 交</a-button>
    </div>
  </div>
</template>

<script lang="ts">

  import {BasicForm, useForm} from '/@/components/Form/index';
  import { computed, defineComponent, reactive, ref, unref } from 'vue';
  import {defHttp} from '/@/utils/http/axios';
  import { propTypes } from '/@/utils/propTypes';
  import { useJvxeMethod } from '/@/hooks/system/useJvxeMethods';
  import { VALIDATE_FAILED } from '/@/utils/common/vxeUtils';
  import {getBpmFormSchema,cesOrderGoodsColumns,cesOrderCustomerColumns} from '../CesOrderMain.data';
  import {saveOrUpdate,cesOrderGoodsList,cesOrderCustomerList} from '../CesOrderMain.api';

  export default defineComponent({
    name: "CesOrderMainForm",
    components:{
      BasicForm,
    },
    props:{
      formData: propTypes.object.def({}),
      formBpm: propTypes.bool.def(true),
    },
    setup(props){
      const [registerForm, { setFieldsValue, setProps }] = useForm({
        labelWidth: 150,
        schemas: getBpmFormSchema(props.formData),
        showActionButtonGroup: false,
        baseColProps: {span: 24}
      });

      const formDisabled = computed(()=>{
        if(props.formData.disabled === false){
          return false;
        }
        return true;
      });

      const refKeys = ref(['cesOrderGoods', 'cesOrderCustomer', ]);
      const activeKey = ref('cesOrderGoods');
      const cesOrderGoods = ref();
      const cesOrderCustomer = ref();
      const tableRefs = {cesOrderGoods, cesOrderCustomer, };
      const cesOrderGoodsTable = reactive({
        loading: false,
        dataSource: [],
        columns:cesOrderGoodsColumns,
        show: false
      })
      const cesOrderCustomerTable = reactive({
        loading: false,
        dataSource: [],
        columns:cesOrderCustomerColumns,
        show: false
      })

      const [handleChangeTabs,handleSubmit,requestSubTableData,formRef] = useJvxeMethod(requestAddOrEdit,classifyIntoFormData,tableRefs,activeKey,refKeys,validateSubForm);

      function classifyIntoFormData(allValues) {
        let main = Object.assign({}, allValues.formValue)
        return {
          ...main, // 展开
          cesOrderGoodsList: allValues.tablesValue[0].tableData,
          cesOrderCustomerList: allValues.tablesValue[1].tableData,
        }
      }

      //表单提交事件
      async function requestAddOrEdit(values) {
        await saveOrUpdate(values, true);
      }

      const queryByIdUrl = '/main/cesOrderMain/queryById';
      async function initFormData(){
        let params = {id: props.formData.dataId};
        const data = await defHttp.get({url: queryByIdUrl, params});
        //设置表单的值
        await setFieldsValue({...data});
        requestSubTableData(cesOrderGoodsList, {id: data.id}, cesOrderGoodsTable, ()=>{
          cesOrderGoodsTable.show = true;
        });
        requestSubTableData(cesOrderCustomerList, {id: data.id}, cesOrderCustomerTable, ()=>{
          cesOrderCustomerTable.show = true;
        });
        //默认是禁用
        await setProps({disabled: formDisabled.value})
      }

      initFormData();

      return {
        registerForm,
        formDisabled,
        formRef,
        handleSubmit,
        activeKey,
        handleChangeTabs,
        cesOrderGoods,
        cesOrderCustomer,
        cesOrderGoodsTable,
        cesOrderCustomerTable,
      }
    }
  });
</script>
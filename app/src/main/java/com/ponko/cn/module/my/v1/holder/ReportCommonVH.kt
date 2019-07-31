package com.ponko.cn.module.my.v1.holder

import android.content.Context
import android.graphics.Color
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.ponko.cn.R
import com.ponko.cn.bean.ReportCommonBean
import com.ponko.cn.bean.StudyReportCBean
import com.ponko.cn.utils.ToastUtil
import com.xm.lib.common.base.rv.BaseRvAdapter
import com.xm.lib.common.base.rv.BaseViewHolder
import com.xm.lib.common.util.ScreenUtil
import com.xm.lib.component.test.CharActivity

/**
 * 学习报告 - 最近&累计 ViewHolder
 */
class ReportCommonVH(view: View) : BaseViewHolder(view) {

    private var ui: ReportCommonUI? = null

    override fun bindData(d: Any, position: Int) {

        //查找组件
        if (ui == null) {
            ui = ReportCommonUI.create(itemView)
        }
        val ctx = itemView.context
        if (d is ReportCommonBean) {
            val reportCommonBean = d as ReportCommonBean
            //设置标题
            ui?.tvTitle?.text = reportCommonBean.title

            //为RecyclerView设置适配器以及展示的样式
            val adapter = object : BaseRvAdapter() {}
            addDataToAdapter(reportCommonBean, adapter) //添加数据到适配器中
            ui?.rv?.adapter = adapter
            ui?.rv?.layoutManager = LinearLayoutManager(ctx)
            ui?.rv?.isFocusableInTouchMode = false
        } else {
            ToastUtil.show("d类型转化到ReportCommonBean失败：$d")
        }
    }

    private fun addDataToAdapter(reportCommonBean: ReportCommonBean, adapter: BaseRvAdapter) {
        var viewType = 0
        if (reportCommonBean.list?.isNotEmpty()!!) {
            for (item in reportCommonBean.list!!) {
                adapter.addItemViewDelegate(viewType, ItemVH::class.java, ItemBean::class.java, R.layout.item_report_item)
                adapter.data?.add(ItemBean(item))
                viewType++
            }
        }

        //折线图
        if (reportCommonBean.report != null) {
            adapter.addItemViewDelegate(viewType, CharVH::class.java, CharBean::class.java, R.layout.item_report_char)
            adapter.data?.add(CharBean(reportCommonBean.report!!))
            viewType++
        }

        //底部显示
        if (!TextUtils.isEmpty(reportCommonBean.footer)) {
            adapter.addItemViewDelegate(viewType, FooterVH::class.java, FooterBean::class.java, R.layout.item_report_footer)
            adapter.data?.add(FooterBean(reportCommonBean.footer))
        }
    }

    /**
     * ViewHolder
     */
    open class ItemVH(view: View) : BaseViewHolder(view) {

        private var itemUI: ItemUI? = null

        override fun bindData(d: Any, position: Int) {
            if (itemUI == null) {
                itemUI = ItemUI.create(itemView)
            }
            val itemBean = d as ItemBean
            itemUI?.tvName?.text = itemBean.item.key
            itemUI?.tvNum?.text = itemBean.item.value.toString()
            itemUI?.tvUnit?.text = itemBean.item.unit
        }

        /**
         *  item 界面
         */
        private class ItemUI private constructor(val tvName: TextView, val tvNum: TextView, val tvUnit: TextView) {
            companion object {

                fun create(rootView: View): ItemUI {
                    val tvName = rootView.findViewById<View>(R.id.tv_name) as TextView
                    val tvNum = rootView.findViewById<View>(R.id.tv_num) as TextView
                    val tvUnit = rootView.findViewById<View>(R.id.tv_unit) as TextView
                    return ItemUI(tvName, tvNum, tvUnit)
                }
            }
        }

    }

    open class CharVH(view: View) : BaseViewHolder(view) {

        private var charUI: CharUI? = null

        override fun bindData(d: Any, position: Int) {
            if (charUI == null) {
                charUI = CharUI.create(itemView)
            }
            val charBean = d as CharBean
            charUI?.tv7day?.text = charBean.report.title
            charUI?.tvUnit?.text = charBean.report.subtitle
            chartTest(itemView.context, charBean.report.list)
        }

        private fun chartTest(ctx: Context, list: MutableList<StudyReportCBean.ListBeanXX.ReportBean.PointBean>) {
            val chart = charUI?.chart
            val dataObjects = arrayOf(0, 0, 0, 0, 0, 0, 0, 0)

            for ((count, point) in list.withIndex()) {
                if (count < list.size) {
                    dataObjects[count] = point.y
                }
            }

            val entries = ArrayList<Entry>()
            for (i in dataObjects.indices) {
                val data = dataObjects[i]
                entries.add(Entry(i.toFloat(), data.toFloat()))
            }
            val dataSet = LineDataSet(entries, "")
            dataSet.setColors(Color.parseColor("#CFA972")) // 每个点之间线的颜色，还有其他几个方法，自己看
            dataSet.setDrawHorizontalHighlightIndicator(false) //是否绘制十字指示线的 【横线】
            dataSet.setCircleColor(Color.parseColor("#CFA972"))//设置圆圈颜色
            dataSet.valueTextColor = Color.parseColor("#676767")//设置文字大小
            dataSet.valueTextSize = 12f  //设置文字大小

            //十字线设置
            val lineData = LineData(dataSet)
            lineData.isHighlightEnabled = true  //是否高亮
            chart?.data = lineData

            //不绘制左边右边刻度线
            chart?.axisRight?.isEnabled = false
            chart?.axisLeft?.isEnabled = false

            //XY轴设置相关
            val xAxis = chart?.xAxis    // 获取X轴
            xAxis?.setDrawAxisLine(false)    // 是否绘制坐标轴,默认true
            xAxis?.gridColor = Color.parseColor("#DDDDDD")
            xAxis?.textColor = Color.parseColor("#333333")
            xAxis?.textSize = 12f
            xAxis?.labelCount = 8
            val labels = ArrayList<String>()
            for (point in list) {
                labels.add(point.x)
            }

            xAxis?.granularity = (1).toFloat()   //颗粒度（单位天）
            xAxis?.position = XAxis.XAxisPosition.BOTTOM
            xAxis?.valueFormatter = CharActivity.CustomXValueFromatter(labels)

            val yAxis = chart?.axisLeft
            yAxis?.setDrawZeroLine(false) //
            yAxis?.gridColor = Color.parseColor("#DDDDDD")
            //yAxis?.spaceBottom = 12f
            yAxis?.spaceTop = 15f

            //设置动画
            chart?.animateY(800)

            //是否缩放
            chart?.isScaleYEnabled = false
            chart?.isScaleXEnabled = false

            val markerView = CharActivity.CustomMarkerView(ctx, com.xm.lib.component.R.layout.char_marker_view)
            markerView.chartView = chart
            chart?.marker = markerView

            chart?.legend?.formSize = ScreenUtil.dip2px(ctx, 0).toFloat()
            chart?.legend?.textColor = Color.parseColor("#676767")
            chart?.legend?.textSize = 13f
            chart?.legend?.xEntrySpace = 50f; //X轴图例间距
            chart?.legend?.yEntrySpace = 50f; //Y轴图例间距
            chart?.legend?.formToTextSpace = 50f; //图例和说明文字之间的间距

            val description = Description()
            description.text = ""
            chart?.description = description
        }

        /**
         *  item 折线图界面
         */
        private class CharUI private constructor(val clTitle: ConstraintLayout, val tv7day: TextView, val tvUnit: TextView, val chart: LineChart) {
            companion object {

                fun create(rootView: View): CharUI {
                    val clTitle = rootView.findViewById<View>(R.id.cl_title) as ConstraintLayout
                    val tv7day = rootView.findViewById<View>(R.id.tv_7day) as TextView
                    val tvUnit = rootView.findViewById<View>(R.id.tv_unit) as TextView
                    val chart = rootView.findViewById<View>(R.id.chart) as LineChart
                    return CharUI(clTitle, tv7day, tvUnit, chart)
                }
            }
        }

    }

    open class FooterVH(view: View) : BaseViewHolder(view) {
        private var footerUI: FooterUI? = null

        override fun bindData(d: Any, position: Int) {
            if (footerUI == null) {
                footerUI = FooterUI.create(itemView)
            }
            val footerBean = d as FooterBean

            val startIndex = footerBean.footer?.indexOf("<red>")
            val endIndex = footerBean.footer?.lastIndexOf("</red>")
            //"今日学习力打败了<red>86%</red>的同学！"
            footerUI?.tv1?.text = footerBean.footer?.substring(0, startIndex!!)
            footerUI?.tv2?.text = footerBean.footer?.substring(startIndex!! + 5, endIndex!!)
            footerUI?.tv3?.text = footerBean.footer?.substring(endIndex!! + 6, footerBean.footer.length)
        }

        /**
         *  item Footer界面
         */
        private class FooterUI private constructor(val rootView: View, val tv1: TextView, val tv2: TextView, val tv3: TextView) {
            companion object {

                fun create(rootView: View): FooterUI {
                    val tv1 = rootView.findViewById<View>(R.id.tv_1) as TextView
                    val tv2 = rootView.findViewById<View>(R.id.tv_2) as TextView
                    val tv3 = rootView.findViewById<View>(R.id.tv_3) as TextView
                    return FooterUI(rootView, tv1, tv2, tv3)
                }
            }
        }

    }

    /**
     * 实体
     */
    class ItemBean(val item: StudyReportCBean.ListBeanXX.BasicInfo)

    class CharBean(val report: StudyReportCBean.ListBeanXX.ReportBean)

    class FooterBean(val footer: String?)

    /**
     *  ReportCommon界面
     */
    private class ReportCommonUI private constructor(val clTitle: ConstraintLayout, val tvTitle: TextView, val tvMore: TextView, val rv: RecyclerView) {
        companion object {

            fun create(rootView: View): ReportCommonUI {
                val clTitle = rootView.findViewById<View>(R.id.cl_title) as ConstraintLayout
                val tvTitle = rootView.findViewById<View>(R.id.tv_title) as TextView
                val tvMore = rootView.findViewById<View>(R.id.tv_more) as TextView
                val rv = rootView.findViewById<View>(R.id.rv) as RecyclerView
                return ReportCommonUI(clTitle, tvTitle, tvMore, rv)
            }
        }
    }
}
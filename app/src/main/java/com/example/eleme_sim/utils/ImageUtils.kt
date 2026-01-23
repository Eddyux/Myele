package com.example.eleme_sim.utils

import com.example.eleme_sim.R

/**
 * 图片资源工具类
 */
object ImageUtils {
    /**
     * 根据餐厅名称获取对应的图片资源ID
     */
    fun getRestaurantImage(restaurantName: String): Int {
        return when {
            restaurantName.contains("川香麻辣烫") -> R.drawable.chuanxiang_malatang
            restaurantName.contains("老北京炸酱面") -> R.drawable.laobeijing_zhajangmian
            restaurantName.contains("湘味轩") -> R.drawable.xiangweixuan
            restaurantName.contains("粤式早茶") -> R.drawable.yueshi_zaocha
            restaurantName.contains("韩式炸鸡") -> R.drawable.hanshi_zhaji
            restaurantName.contains("瑞幸咖啡") -> R.drawable.luckin_coffee
            restaurantName.contains("茶百道") -> R.drawable.chabaidao
            restaurantName.contains("蜜雪冰城") -> R.drawable.mixue
            restaurantName.contains("星巴克") -> R.drawable.starbucks
            restaurantName.contains("喜茶") -> R.drawable.heytea
            restaurantName.contains("金长风荷叶烤鸡") -> R.drawable.jinchangfeng_heyekaoji
            restaurantName.contains("北京烤鸭") -> R.drawable.bjky
            restaurantName.contains("美滋滋烤肉拌饭") -> R.drawable.meizizi_krbf
            restaurantName.contains("肯德基") || restaurantName.contains("KFC") -> R.drawable.kfc
            restaurantName.contains("CoCo") || restaurantName.contains("coco") -> R.drawable.coco
            restaurantName.contains("麦当劳") -> R.drawable.maidanglao
            restaurantName.contains("必胜客") -> R.drawable.bishengke
            restaurantName.contains("海底捞") -> R.drawable.haidilao
            restaurantName.contains("西贝") -> R.drawable.xibeiyoumiancun
            restaurantName.contains("外婆家") -> R.drawable.waipojia
            restaurantName.contains("黄焖鸡") -> R.drawable.huangmenjimifan
            restaurantName.contains("沙县") -> R.drawable.shaxianxiaochi
            restaurantName.contains("兰州拉面") -> R.drawable.lanzhoulamian
            else -> R.drawable.tongyong // 默认返回通用图片
        }
    }

    /**
     * 获取用户头像
     */
    fun getUserAvatar(): Int {
        return R.drawable.user_avatar
    }

    /**
     * 获取骑手头像
     */
    fun getRiderAvatar(): Int {
        return R.drawable.rider_avatar
    }

    /**
     * 获取爆品团图片
     * @param index 图片索引，0-4分别对应hot_deal.png和hot_deal_1.png到hot_deal_4.png
     */
    fun getHotDealImage(index: Int = 0): Int {
        return when (index) {
            0 -> R.drawable.hot_deal
            1 -> R.drawable.hot_deal_1
            2 -> R.drawable.hot_deal_2
            3 -> R.drawable.hot_deal_3
            4 -> R.drawable.hot_deal_4
            else -> R.drawable.hot_deal // 默认返回第一张
        }
    }

    /**
     * 获取所有爆品团图片列表
     */
    fun getAllHotDealImages(): List<Int> {
        return listOf(
            R.drawable.hot_deal,
            R.drawable.hot_deal_1,
            R.drawable.hot_deal_2,
            R.drawable.hot_deal_3,
            R.drawable.hot_deal_4
        )
    }

    /**
     * 根据商品ID获取对应的图片资源ID
     */
    fun getProductImage(productId: String): Int {
        return when (productId) {
            // CoCo都可
            "prod_151" -> R.drawable.caipin_cocodoukebobanaicha
            "prod_152" -> R.drawable.caipin_cocodoukehongdounaicha
            "prod_153" -> R.drawable.caipin_cocodoukebudingnaicha
            "prod_154" -> R.drawable.caipin_cocodoukeyeguonaicha
            "prod_155" -> R.drawable.caipin_cocodoukeyuyuannaicha
            "prod_156" -> R.drawable.caipin_cocodoukexiancaonaicha
            "prod_157" -> R.drawable.caipin_cocodoukeningmengcha
            "prod_158" -> R.drawable.caipin_cocodoukeshuiguocha
            "prod_159" -> R.drawable.caipin_cocodoukenaigaicha
            "prod_160" -> R.drawable.caipin_cocodoukezhishinaigai
            "prod_161" -> R.drawable.caipin_cocodoukesijichuncha
            "prod_162" -> R.drawable.caipin_cocodoukewulongcha
            "prod_163" -> R.drawable.caipin_cocodoukeguihuawulong
            "prod_164" -> R.drawable.caipin_cocodoukemolilvcha
            "prod_165" -> R.drawable.caipin_cocodoukehongchanatie
            "prod_396" -> R.drawable.caipin_cocodouketaocana
            "prod_397" -> R.drawable.caipin_cocodouketaocanb
            "prod_398" -> R.drawable.caipin_cocodouketeseyinpin
            "prod_399" -> R.drawable.caipin_cocodoukejingpinxiaoshi
            "prod_400" -> R.drawable.caipin_cocodouketesetianpin
            // 兰州拉面
            "prod_286" -> R.drawable.caipin_lanzhoulamianniuroulamian
            "prod_287" -> R.drawable.caipin_lanzhoulamianyangroulamian
            "prod_288" -> R.drawable.caipin_lanzhoulamianfeichangmian
            "prod_289" -> R.drawable.caipin_lanzhoulamiansuanlafen
            "prod_290" -> R.drawable.caipin_lanzhoulamiandaoxiaomian
            "prod_291" -> R.drawable.caipin_lanzhoulamiansaozimian
            "prod_292" -> R.drawable.caipin_lanzhoulamianzhajiangmian
            "prod_293" -> R.drawable.caipin_lanzhoulamianreganmian
            "prod_294" -> R.drawable.caipin_lanzhoulamiandandanmian
            "prod_295" -> R.drawable.caipin_lanzhoulamianliangmian
            "prod_296" -> R.drawable.caipin_lanzhoulamianchaomian
            "prod_297" -> R.drawable.caipin_lanzhoulamianlanzhoulamiantesecai1
            "prod_298" -> R.drawable.caipin_lanzhoulamianlanzhoulamiantesecai2
            "prod_299" -> R.drawable.caipin_lanzhoulamianlanzhoulamiantesecai3
            "prod_300" -> R.drawable.caipin_lanzhoulamianlanzhoulamiantesecai4
            "prod_441" -> R.drawable.caipin_lanzhoulamianzhaopaitaocana
            "prod_442" -> R.drawable.caipin_lanzhoulamianzhaopaitaocanb
            "prod_443" -> R.drawable.caipin_lanzhoulamianteseyinpin
            "prod_444" -> R.drawable.caipin_lanzhoulamianjingpinxiaoshi
            "prod_445" -> R.drawable.caipin_lanzhoulamiantesetianpin
            // 北京烤鸭
            "prod_301" -> R.drawable.caipin_beijingkaoyazhaopaibeijingkaoyazhengzhi
            "prod_302" -> R.drawable.caipin_beijingkaoyabeijingkaoyabanzhi
            "prod_303" -> R.drawable.caipin_beijingkaoyapianpikaoyataocan
            "prod_304" -> R.drawable.caipin_beijingkaoyayajiatang
            "prod_305" -> R.drawable.caipin_beijingkaoyajingjiangrousi
            "prod_306" -> R.drawable.caipin_beijingkaoyalaobeijingzhajiangmian
            "prod_307" -> R.drawable.caipin_beijingkaoyagongbaojiding
            "prod_308" -> R.drawable.caipin_beijingkaoyaganzhawanzi
            "prod_309" -> R.drawable.caipin_beijingkaoyachaoganer
            "prod_310" -> R.drawable.caipin_beijingkaoyalvdagun
            "prod_311" -> R.drawable.caipin_beijingkaoyadouzhiertaocan
            "prod_312" -> R.drawable.caipin_beijingkaoyajiemoyazhang
            "prod_313" -> R.drawable.caipin_beijingkaoyatangcupaigu
            "prod_314" -> R.drawable.caipin_beijingkaoyabaodu
            "prod_315" -> R.drawable.caipin_beijingkaoyasuanmeitang
            "prod_446" -> R.drawable.caipin_beijingkaoyazhaopaitaocana
            "prod_447" -> R.drawable.caipin_beijingkaoyazhaopaitaocanb
            "prod_448" -> R.drawable.caipin_beijingkaoyateseyinpin
            "prod_449" -> R.drawable.caipin_beijingkaoyajingpinxiaoshi
            "prod_450" -> R.drawable.caipin_beijingkaoyatesetianpin
            // 喜茶
            "prod_017" -> R.drawable.caipin_xichazhizhitaotao
            "prod_018" -> R.drawable.caipin_xichayunibobocha
            "prod_040" -> R.drawable.caipin_xichamangmangganlu
            "prod_139" -> R.drawable.caipin_xichabobanaicha
            "prod_140" -> R.drawable.caipin_xichahongdounaicha
            "prod_141" -> R.drawable.caipin_xichabudingnaicha
            "prod_142" -> R.drawable.caipin_xichayeguonaicha
            "prod_143" -> R.drawable.caipin_xichayuyuannaicha
            "prod_144" -> R.drawable.caipin_xichaxiancaonaicha
            "prod_145" -> R.drawable.caipin_xichaningmengcha
            "prod_146" -> R.drawable.caipin_xichashuiguocha
            "prod_147" -> R.drawable.caipin_xichanaigaicha
            "prod_148" -> R.drawable.caipin_xichazhishinaigai
            "prod_149" -> R.drawable.caipin_xichasijichuncha
            "prod_150" -> R.drawable.caipin_xichawulongcha
            "prod_391" -> R.drawable.caipin_xichaduorouputao
            "prod_392" -> R.drawable.caipin_xichazhizhimeimei
            "prod_393" -> R.drawable.caipin_xichamangyenuomifan
            "prod_394" -> R.drawable.caipin_xichamanbeihongyou
            "prod_395" -> R.drawable.caipin_xichakaoheitangboboxiannai
            // 外婆家
            "prod_241" -> R.drawable.caipin_waipojiaxihucuyu
            "prod_242" -> R.drawable.caipin_waipojiadongporou
            "prod_243" -> R.drawable.caipin_waipojialongjingxiaren
            "prod_244" -> R.drawable.caipin_waipojiasongshuguiyu
            "prod_245" -> R.drawable.caipin_waipojiajiaohuaji
            "prod_246" -> R.drawable.caipin_waipojiahongshaoshizitou
            "prod_247" -> R.drawable.caipin_waipojiaqingzhengluyu
            "prod_248" -> R.drawable.caipin_waipojiatangcuxiaopai
            "prod_249" -> R.drawable.caipin_waipojiayanshuiya
            "prod_250" -> R.drawable.caipin_waipojiaxiefendoufu
            "prod_251" -> R.drawable.caipin_waipojiayoumenchunsun
            "prod_252" -> R.drawable.caipin_waipojiawaipojiatesecai1
            "prod_253" -> R.drawable.caipin_waipojiawaipojiatesecai2
            "prod_254" -> R.drawable.caipin_waipojiawaipojiatesecai3
            "prod_255" -> R.drawable.caipin_waipojiawaipojiatesecai4
            "prod_426" -> R.drawable.caipin_waipojiazhaopaitaocana
            "prod_427" -> R.drawable.caipin_waipojiazhaopaitaocanb
            "prod_428" -> R.drawable.caipin_waipojiateseyinpin
            "prod_429" -> R.drawable.caipin_waipojiajingpinxiaoshi
            "prod_430" -> R.drawable.caipin_waipojiatesetianpin
            // 川香麻辣烫
            "prod_001" -> R.drawable.caipin_chuanxiangmalatangmalatang
            "prod_006" -> R.drawable.caipin_chuanxiangmalatangkele
            "prod_019" -> R.drawable.caipin_chuanxiangmalatangtudoufen
            "prod_020" -> R.drawable.caipin_chuanxiangmalatanghongyouchaoshou
            "prod_021" -> R.drawable.caipin_chuanxiangmalatangsuanlafen
            "prod_041" -> R.drawable.caipin_chuanxiangmalatangfuqifeipian
            "prod_042" -> R.drawable.caipin_chuanxiangmalatangshuizhuyu
            "prod_043" -> R.drawable.caipin_chuanxiangmalatanghuiguorou
            "prod_044" -> R.drawable.caipin_chuanxiangmalatanggongbaojiding
            "prod_045" -> R.drawable.caipin_chuanxiangmalatangyuxiangrousi
            "prod_046" -> R.drawable.caipin_chuanxiangmalatangmapodoufu
            "prod_047" -> R.drawable.caipin_chuanxiangmalatangdandanmian
            "prod_048" -> R.drawable.caipin_chuanxiangmalatangzhongshuijiao
            "prod_049" -> R.drawable.caipin_chuanxiangmalatanglongchaoshou
            "prod_050" -> R.drawable.caipin_chuanxiangmalatangmaocai
            "prod_346" -> R.drawable.caipin_chuanxiangmalatangmalaniurou
            "prod_347" -> R.drawable.caipin_chuanxiangmalatangxianxiamalatang
            "prod_348" -> R.drawable.caipin_chuanxiangmalatangsucaipinpan
            "prod_349" -> R.drawable.caipin_chuanxiangmalatangmaodumalatang
            "prod_350" -> R.drawable.caipin_chuanxiangmalatangdoufupimalatang
            // 必胜客
            "prod_196" -> R.drawable.caipin_bishengkepisa
            "prod_197" -> R.drawable.caipin_bishengkeyidalimian
            "prod_198" -> R.drawable.caipin_bishengkejufan
            "prod_199" -> R.drawable.caipin_bishengkeniupai
            "prod_200" -> R.drawable.caipin_bishengkeshala
            "prod_201" -> R.drawable.caipin_bishengkenongtang
            "prod_202" -> R.drawable.caipin_bishengkekaochi
            "prod_203" -> R.drawable.caipin_bishengkekaoji
            "prod_204" -> R.drawable.caipin_bishengkezhishidangao
            "prod_205" -> R.drawable.caipin_bishengketilamisu
            "prod_206" -> R.drawable.caipin_bishengkehaixianpinpan
            "prod_207" -> R.drawable.caipin_bishengkeyangpai
            "prod_208" -> R.drawable.caipin_bishengkeegan
            "prod_209" -> R.drawable.caipin_bishengkefashijuwoniu
            "prod_210" -> R.drawable.caipin_bishengkenaiyoumogutang
            "prod_411" -> R.drawable.caipin_bishengkezhaopaitaocana
            "prod_412" -> R.drawable.caipin_bishengkezhaopaitaocanb
            "prod_413" -> R.drawable.caipin_bishengketeseyinpin
            "prod_414" -> R.drawable.caipin_bishengkejingpinxiaoshi
            "prod_415" -> R.drawable.caipin_bishengketesetianpin
            // 星巴克
            "prod_015" -> R.drawable.caipin_xingbakexiangcaonatie
            "prod_016" -> R.drawable.caipin_xingbakezhenguonatie
            "prod_039" -> R.drawable.caipin_xingbakenongqingmoka
            "prod_127" -> R.drawable.caipin_xingbakekabuqinuo
            "prod_128" -> R.drawable.caipin_xingbakenatiekafei
            "prod_129" -> R.drawable.caipin_xingbakemeishikafei
            "prod_130" -> R.drawable.caipin_xingbakemokakafei
            "prod_131" -> R.drawable.caipin_xingbakenongsuokafei
            "prod_132" -> R.drawable.caipin_xingbakebaikafei
            "prod_133" -> R.drawable.caipin_xingbakebingmeishi
            "prod_134" -> R.drawable.caipin_xingbakelengcuikafei
            "prod_135" -> R.drawable.caipin_xingbakeqipaomeishi
            "prod_136" -> R.drawable.caipin_xingbakeyeyunnatie
            "prod_137" -> R.drawable.caipin_xingbakesirongnatie
            "prod_138" -> R.drawable.caipin_xingbakeyanmainatie
            "prod_386" -> R.drawable.caipin_xingbakejiaotangmaqiduo
            "prod_387" -> R.drawable.caipin_xingbakemochanatie
            "prod_388" -> R.drawable.caipin_xingbakefuruibai
            "prod_389" -> R.drawable.caipin_xingbakebingyaohongmeiheijialun
            "prod_390" -> R.drawable.caipin_xingbakebaitaoxingbingle
            // 沙县小吃
            "prod_271" -> R.drawable.caipin_shaxianxiaochibanmian
            "prod_272" -> R.drawable.caipin_shaxianxiaochichaofen
            "prod_273" -> R.drawable.caipin_shaxianxiaochiyuntun
            "prod_274" -> R.drawable.caipin_shaxianxiaochizhengjiao
            "prod_275" -> R.drawable.caipin_shaxianxiaochijianjiao
            "prod_276" -> R.drawable.caipin_shaxianxiaochixiaolongbao
            "prod_277" -> R.drawable.caipin_shaxianxiaochidunguan
            "prod_278" -> R.drawable.caipin_shaxianxiaochiluwei
            "prod_279" -> R.drawable.caipin_shaxianxiaochiliangbancai
            "prod_280" -> R.drawable.caipin_shaxianxiaochidanchaofan
            "prod_281" -> R.drawable.caipin_shaxianxiaochiyangzhouchaofan
            "prod_282" -> R.drawable.caipin_shaxianxiaochishaxianxiaochitesecai1
            "prod_283" -> R.drawable.caipin_shaxianxiaochishaxianxiaochitesecai2
            "prod_284" -> R.drawable.caipin_shaxianxiaochishaxianxiaochitesecai3
            "prod_285" -> R.drawable.caipin_shaxianxiaochishaxianxiaochitesecai4
            "prod_436" -> R.drawable.caipin_shaxianxiaochizhaopaitaocana
            "prod_437" -> R.drawable.caipin_shaxianxiaochizhaopaitaocanb
            "prod_438" -> R.drawable.caipin_shaxianxiaochiteseyinpin
            "prod_439" -> R.drawable.caipin_shaxianxiaochijingpinxiaoshi
            "prod_440" -> R.drawable.caipin_shaxianxiaochitesetianpin
            // 海底捞火锅
            "prod_211" -> R.drawable.caipin_haidilaohuoguoniuroujuan
            "prod_212" -> R.drawable.caipin_haidilaohuoguoyangroujuan
            "prod_213" -> R.drawable.caipin_haidilaohuoguomaodu
            "prod_214" -> R.drawable.caipin_haidilaohuoguoyachang
            "prod_215" -> R.drawable.caipin_haidilaohuoguoxiahua
            "prod_216" -> R.drawable.caipin_haidilaohuoguoniubaiye
            "prod_217" -> R.drawable.caipin_haidilaohuoguowucanrou
            "prod_218" -> R.drawable.caipin_haidilaohuoguonenniurou
            "prod_219" -> R.drawable.caipin_haidilaohuoguofeiniu
            "prod_220" -> R.drawable.caipin_haidilaohuoguoqiancengdu
            "prod_221" -> R.drawable.caipin_haidilaohuoguoechang
            "prod_222" -> R.drawable.caipin_haidilaohuoguohuanghou
            "prod_223" -> R.drawable.caipin_haidilaohuoguonaohua
            "prod_224" -> R.drawable.caipin_haidilaohuoguoyaxue
            "prod_225" -> R.drawable.caipin_haidilaohuoguodoufu
            "prod_416" -> R.drawable.caipin_haidilaohuoguozhaopaitaocana
            "prod_417" -> R.drawable.caipin_haidilaohuoguozhaopaitaocanb
            "prod_418" -> R.drawable.caipin_haidilaohuoguoteseyinpin
            "prod_419" -> R.drawable.caipin_haidilaohuoguojingpinxiaoshi
            "prod_420" -> R.drawable.caipin_haidilaohuoguotesetianpin
            // 湘味轩
            "prod_003" -> R.drawable.caipin_xiangweixuanxiangshixiaochaorou
            "prod_008" -> R.drawable.caipin_xiangweixuanmaoxuewang
            "prod_025" -> R.drawable.caipin_xiangweixuannongjiaxiaochaorou
            "prod_026" -> R.drawable.caipin_xiangweixuansuandoujiaochaoroumo
            "prod_027" -> R.drawable.caipin_xiangweixuanxiangxisuanrou
            "prod_062" -> R.drawable.caipin_xiangweixuansuancaiyu
            "prod_063" -> R.drawable.caipin_xiangweixuanlajiaochaorou
            "prod_064" -> R.drawable.caipin_xiangweixuanwaipocai
            "prod_065" -> R.drawable.caipin_xiangweixuanduojiaoji
            "prod_066" -> R.drawable.caipin_xiangweixuanganguofeichang
            "prod_067" -> R.drawable.caipin_xiangweixuanlaweihezheng
            "prod_068" -> R.drawable.caipin_xiangweixuanandongji
            "prod_069" -> R.drawable.caipin_xiangweixuanxiangxitufeiji
            "prod_070" -> R.drawable.caipin_xiangweixuanshousibaocai
            "prod_071" -> R.drawable.caipin_xiangweixuantangcupaigu
            "prod_356" -> R.drawable.caipin_xiangweixuanduojiaoyutou
            "prod_357" -> R.drawable.caipin_xiangweixuanxiaochaohuangniurou
            "prod_358" -> R.drawable.caipin_xiangweixuanxiangxiwaipocai
            "prod_359" -> R.drawable.caipin_xiangweixuankouweixia
            "prod_360" -> R.drawable.caipin_xiangweixuanxiangweizhengcai
            // 瑞幸咖啡
            "prod_009" -> R.drawable.caipin_ruixingkafeiruinabing
            "prod_010" -> R.drawable.caipin_ruixingkafeimokakekesuisuibing
            "prod_034" -> R.drawable.caipin_ruixingkafeihaiyanzhishinatie
            "prod_035" -> R.drawable.caipin_ruixingkafeiyunshinatie
            "prod_093" -> R.drawable.caipin_ruixingkafeikabuqinuo
            "prod_094" -> R.drawable.caipin_ruixingkafeinatiekafei
            "prod_095" -> R.drawable.caipin_ruixingkafeimeishikafei
            "prod_096" -> R.drawable.caipin_ruixingkafeimokakafei
            "prod_097" -> R.drawable.caipin_ruixingkafeinongsuokafei
            "prod_098" -> R.drawable.caipin_ruixingkafeibaikafei
            "prod_099" -> R.drawable.caipin_ruixingkafeibingmeishi
            "prod_100" -> R.drawable.caipin_ruixingkafeilengcuikafei
            "prod_101" -> R.drawable.caipin_ruixingkafeiqipaomeishi
            "prod_102" -> R.drawable.caipin_ruixingkafeiyeyunnatie
            "prod_103" -> R.drawable.caipin_ruixingkafeixiaolunatie
            "prod_371" -> R.drawable.caipin_ruixingkafeishengyenatie
            "prod_372" -> R.drawable.caipin_ruixingkafeisirongnatie
            "prod_373" -> R.drawable.caipin_ruixingkafeichengcmeishi
            "prod_374" -> R.drawable.caipin_ruixingkafeihourunatie
            "prod_375" -> R.drawable.caipin_ruixingkafeiyanmainatie
            // 粤式早茶
            "prod_004" -> R.drawable.caipin_yueshizaochaxiajiao
            "prod_007" -> R.drawable.caipin_yueshizaochashaoe
            "prod_028" -> R.drawable.caipin_yueshizaochaheyenuomiji
            "prod_029" -> R.drawable.caipin_yueshizaochajinshanaihuangbao
            "prod_030" -> R.drawable.caipin_yueshizaochashuijingxiajiao
            "prod_072" -> R.drawable.caipin_yueshizaochabaiqieji
            "prod_073" -> R.drawable.caipin_yueshizaochanuomiji
            "prod_074" -> R.drawable.caipin_yueshizaochashizhizhengpaigu
            "prod_075" -> R.drawable.caipin_yueshizaochapaigu
            "prod_076" -> R.drawable.caipin_yueshizaochashaomai
            "prod_077" -> R.drawable.caipin_yueshizaochadanta
            "prod_078" -> R.drawable.caipin_yueshizaochaluobogao
            "prod_079" -> R.drawable.caipin_yueshizaochamalagao
            "prod_080" -> R.drawable.caipin_yueshizaochanaihuangbao
            "prod_081" -> R.drawable.caipin_yueshizaochapidanshourouzhou
            "prod_361" -> R.drawable.caipin_yueshizaochaxianxiashaomai
            "prod_362" -> R.drawable.caipin_yueshizaochachashaobao
            "prod_363" -> R.drawable.caipin_yueshizaochachangfen
            "prod_364" -> R.drawable.caipin_yueshizaochafengzhao
            "prod_365" -> R.drawable.caipin_yueshizaochaliushabao
            // 美滋滋烤肉拌饭
            "prod_316" -> R.drawable.caipin_meizizikaoroubanfanzhaopaikaoroubanfan
            "prod_317" -> R.drawable.caipin_meizizikaoroubanfanzhishikaoroubanfan
            "prod_318" -> R.drawable.caipin_meizizikaoroubanfanshiguobanfan
            "prod_319" -> R.drawable.caipin_meizizikaoroubanfanbuduihuoguotaocan
            "prod_320" -> R.drawable.caipin_meizizikaoroubanfanhanshizhaji
            "prod_321" -> R.drawable.caipin_meizizikaoroubanfanpaocaibanfan
            "prod_322" -> R.drawable.caipin_meizizikaoroubanfanniuroubanfan
            "prod_323" -> R.drawable.caipin_meizizikaoroubanfanhaixianbanfan
            "prod_324" -> R.drawable.caipin_meizizikaoroubanfanhanshilachaoniangao
            "prod_325" -> R.drawable.caipin_meizizikaoroubanfanzicaibaofan
            "prod_326" -> R.drawable.caipin_meizizikaoroubanfanlabaicai
            "prod_327" -> R.drawable.caipin_meizizikaoroubanfanhaidaitang
            "prod_328" -> R.drawable.caipin_meizizikaoroubanfanpaocaitang
            "prod_329" -> R.drawable.caipin_meizizikaoroubanfanhanshiliangmian
            "prod_330" -> R.drawable.caipin_meizizikaoroubanfanmijiu
            "prod_451" -> R.drawable.caipin_meizizikaoroubanfanzhaopaitaocana
            "prod_452" -> R.drawable.caipin_meizizikaoroubanfanzhaopaitaocanb
            "prod_453" -> R.drawable.caipin_meizizikaoroubanfanteseyinpin
            "prod_454" -> R.drawable.caipin_meizizikaoroubanfanjingpinxiaoshi
            "prod_455" -> R.drawable.caipin_meizizikaoroubanfantesetianpin
            // 老北京炸酱面
            "prod_002" -> R.drawable.caipin_laobeijingzhajiangmianlaobeijingzhajiangmian
            "prod_022" -> R.drawable.caipin_laobeijingzhajiangmianjingjiangrousi
            "prod_023" -> R.drawable.caipin_laobeijingzhajiangmianzhaguanchang
            "prod_024" -> R.drawable.caipin_laobeijingzhajiangmianlaobeijingjiroujuan
            "prod_051" -> R.drawable.caipin_laobeijingzhajiangmianbeijingkaoya
            "prod_052" -> R.drawable.caipin_laobeijingzhajiangmianbaodu
            "prod_053" -> R.drawable.caipin_laobeijingzhajiangmiandouzhier
            "prod_054" -> R.drawable.caipin_laobeijingzhajiangmianlvdagun
            "prod_055" -> R.drawable.caipin_laobeijingzhajiangmianluzhuhuoshao
            "prod_056" -> R.drawable.caipin_laobeijingzhajiangmiantanghulu
            "prod_057" -> R.drawable.caipin_laobeijingzhajiangmiandalianhuoshao
            "prod_058" -> R.drawable.caipin_laobeijingzhajiangmianmendingroubing
            "prod_059" -> R.drawable.caipin_laobeijingzhajiangmianyangxiezi
            "prod_060" -> R.drawable.caipin_laobeijingzhajiangmianwandouhuang
            "prod_061" -> R.drawable.caipin_laobeijingzhajiangmianaiwowo
            "prod_351" -> R.drawable.caipin_laobeijingzhajiangmianjingweidalumian
            "prod_352" -> R.drawable.caipin_laobeijingzhajiangmianlaobeijingliangmian
            "prod_353" -> R.drawable.caipin_laobeijingzhajiangmianzhajiangbanfan
            "prod_354" -> R.drawable.caipin_laobeijingzhajiangmianjingjiangrousimian
            "prod_355" -> R.drawable.caipin_laobeijingzhajiangmianlaobeijinggedatang
            // 肯德基
            "prod_181" -> R.drawable.caipin_kendejihanbao
            "prod_182" -> R.drawable.caipin_kendejizhaji
            "prod_183" -> R.drawable.caipin_kendejishutiao
            "prod_184" -> R.drawable.caipin_kendejijikuai
            "prod_185" -> R.drawable.caipin_kendejijichi
            "prod_186" -> R.drawable.caipin_kendejikele
            "prod_187" -> R.drawable.caipin_kendejinaixi
            "prod_188" -> R.drawable.caipin_kendejishengdai
            "prod_189" -> R.drawable.caipin_kendejipai
            "prod_190" -> R.drawable.caipin_kendejihanbaotaocan
            "prod_191" -> R.drawable.caipin_kendejizaocantaocan
            "prod_192" -> R.drawable.caipin_kendejiertongtaocan
            "prod_193" -> R.drawable.caipin_kendejimaileji
            "prod_194" -> R.drawable.caipin_kendejimailajichi
            "prod_195" -> R.drawable.caipin_kendejiyumibei
            "prod_406" -> R.drawable.caipin_kendejizhaopaitaocana
            "prod_407" -> R.drawable.caipin_kendejizhaopaitaocanb
            "prod_408" -> R.drawable.caipin_kendejiteseyinpin
            "prod_409" -> R.drawable.caipin_kendejijingpinxiaoshi
            "prod_410" -> R.drawable.caipin_kendejitesetianpin
            // 茶百道
            "prod_011" -> R.drawable.caipin_chabaidaoputaoyouyoucha
            "prod_012" -> R.drawable.caipin_chabaidaozhenzhunaicha
            "prod_036" -> R.drawable.caipin_chabaidaoyunibobonaicha
            "prod_037" -> R.drawable.caipin_chabaidaoshaoxiancao
            "prod_104" -> R.drawable.caipin_chabaidaobobanaicha
            "prod_105" -> R.drawable.caipin_chabaidaohongdounaicha
            "prod_106" -> R.drawable.caipin_chabaidaobudingnaicha
            "prod_107" -> R.drawable.caipin_chabaidaoyeguonaicha
            "prod_108" -> R.drawable.caipin_chabaidaoyuyuannaicha
            "prod_109" -> R.drawable.caipin_chabaidaoxiancaonaicha
            "prod_110" -> R.drawable.caipin_chabaidaoningmengcha
            "prod_111" -> R.drawable.caipin_chabaidaoshuiguocha
            "prod_112" -> R.drawable.caipin_chabaidaonaigaicha
            "prod_113" -> R.drawable.caipin_chabaidaozhishinaigai
            "prod_114" -> R.drawable.caipin_chabaidaosijichuncha
            "prod_376" -> R.drawable.caipin_chabaidaoyangzhiganlu
            "prod_377" -> R.drawable.caipin_chabaidaozhaopaiyuyuannaicha
            "prod_378" -> R.drawable.caipin_chabaidaomolinailv
            "prod_379" -> R.drawable.caipin_chabaidaodouruyuqilin
            "prod_380" -> R.drawable.caipin_chabaidaoyunibobolvcha
            // 蜜雪冰城
            "prod_013" -> R.drawable.caipin_mixuebingchengningmengshui
            "prod_014" -> R.drawable.caipin_mixuebingchengbingqilinnaicha
            "prod_038" -> R.drawable.caipin_mixuebingchengbingqilinshengdai
            "prod_115" -> R.drawable.caipin_mixuebingchengbobanaicha
            "prod_116" -> R.drawable.caipin_mixuebingchenghongdounaicha
            "prod_117" -> R.drawable.caipin_mixuebingchengbudingnaicha
            "prod_118" -> R.drawable.caipin_mixuebingchengyeguonaicha
            "prod_119" -> R.drawable.caipin_mixuebingchengyuyuannaicha
            "prod_120" -> R.drawable.caipin_mixuebingchengxiancaonaicha
            "prod_121" -> R.drawable.caipin_mixuebingchengningmengcha
            "prod_122" -> R.drawable.caipin_mixuebingchengshuiguocha
            "prod_123" -> R.drawable.caipin_mixuebingchengnaigaicha
            "prod_124" -> R.drawable.caipin_mixuebingchengzhishinaigai
            "prod_125" -> R.drawable.caipin_mixuebingchengmanbeibaixiangguo
            "prod_126" -> R.drawable.caipin_mixuebingchengwulongcha
            "prod_381" -> R.drawable.caipin_mixuebingchengbingxianningmengshui
            "prod_382" -> R.drawable.caipin_mixuebingchengbingqilintiantong
            "prod_383" -> R.drawable.caipin_mixuebingchengzhenzhunaicha
            "prod_384" -> R.drawable.caipin_mixuebingchengxuewangshengdai
            "prod_385" -> R.drawable.caipin_mixuebingchengsijichuncha
            // 西贝莜面村
            "prod_226" -> R.drawable.caipin_xibeiyoumiancunyoumiankaolaolao
            "prod_227" -> R.drawable.caipin_xibeiyoumiancunyangzatang
            "prod_228" -> R.drawable.caipin_xibeiyoumiancunshouzhuayangrou
            "prod_229" -> R.drawable.caipin_xibeiyoumiancunkaoyangpai
            "prod_230" -> R.drawable.caipin_xibeiyoumiancundapanji
            "prod_231" -> R.drawable.caipin_xibeiyoumiancunhuangmenyangrou
            "prod_232" -> R.drawable.caipin_xibeiyoumiancundunyangrou
            "prod_233" -> R.drawable.caipin_xibeiyoumiancunziranyangrou
            "prod_234" -> R.drawable.caipin_xibeiyoumiancuncongbaoyangrou
            "prod_235" -> R.drawable.caipin_xibeiyoumiancunyangrouchuan
            "prod_236" -> R.drawable.caipin_xibeiyoumiancunyangxiezi
            "prod_237" -> R.drawable.caipin_xibeiyoumiancunxibeiyoumiancuntesecai1
            "prod_238" -> R.drawable.caipin_xibeiyoumiancunxibeiyoumiancuntesecai2
            "prod_239" -> R.drawable.caipin_xibeiyoumiancunxibeiyoumiancuntesecai3
            "prod_240" -> R.drawable.caipin_xibeiyoumiancunxibeiyoumiancuntesecai4
            "prod_421" -> R.drawable.caipin_xibeiyoumiancunzhaopaitaocana
            "prod_422" -> R.drawable.caipin_xibeiyoumiancunzhaopaitaocanb
            "prod_423" -> R.drawable.caipin_xibeiyoumiancunteseyinpin
            "prod_424" -> R.drawable.caipin_xibeiyoumiancunjingpinxiaoshi
            "prod_425" -> R.drawable.caipin_xibeiyoumiancuntesetianpin
            // 金长风荷叶烤鸡
            "prod_331" -> R.drawable.caipin_jinzhangfengheyekaojizhaopaiheyekaojizhengzhi
            "prod_332" -> R.drawable.caipin_jinzhangfengheyekaojiheyekaojibanzhi
            "prod_333" -> R.drawable.caipin_jinzhangfengheyekaojimizhikaojichi
            "prod_334" -> R.drawable.caipin_jinzhangfengheyekaojikaojitui
            "prod_335" -> R.drawable.caipin_jinzhangfengheyekaojikaoquanchi
            "prod_336" -> R.drawable.caipin_jinzhangfengheyekaojiheyekaojitaocan
            "prod_337" -> R.drawable.caipin_jinzhangfengheyekaojikaojizhao
            "prod_338" -> R.drawable.caipin_jinzhangfengheyekaojikaojixin
            "prod_339" -> R.drawable.caipin_jinzhangfengheyekaojijiaoyankaoji
            "prod_340" -> R.drawable.caipin_jinzhangfengheyekaojixianglakaojikuai
            "prod_341" -> R.drawable.caipin_jinzhangfengheyekaojikaojirouchuan
            "prod_342" -> R.drawable.caipin_jinzhangfengheyekaojikaojiruangu
            "prod_343" -> R.drawable.caipin_jinzhangfengheyekaojijitangmixian
            "prod_344" -> R.drawable.caipin_jinzhangfengheyekaojiliangbanjisi
            "prod_345" -> R.drawable.caipin_jinzhangfengheyekaojisuanmeizhi
            "prod_456" -> R.drawable.caipin_jinzhangfengheyekaojizhaopaitaocana
            "prod_457" -> R.drawable.caipin_jinzhangfengheyekaojizhaopaitaocanb
            "prod_458" -> R.drawable.caipin_jinzhangfengheyekaojiteseyinpin
            "prod_459" -> R.drawable.caipin_jinzhangfengheyekaojijingpinxiaoshi
            "prod_460" -> R.drawable.caipin_jinzhangfengheyekaojitesetianpin
            // 韩式炸鸡
            "prod_005" -> R.drawable.caipin_hanshizhaji_hanshizhajiquanjiatong
            "prod_031" -> R.drawable.caipin_hanshizhaji_hanshizhajitui
            "prod_032" -> R.drawable.caipin_hanshizhaji_hanshizhajichi
            "prod_033" -> R.drawable.caipin_hanshizhajiniangaochaolamian
            "prod_082" -> R.drawable.caipin_hanshizhaji_shiguobanfan
            "prod_083" -> R.drawable.caipin_hanshizhaji_buduiguo
            "prod_084" -> R.drawable.caipin_hanshizhajipaocaiguo
            "prod_085" -> R.drawable.caipin_hanshizhaji_hanshikaorou
            "prod_086" -> R.drawable.caipin_hanshizhajilengmian
            "prod_087" -> R.drawable.caipin_hanshizhajizhajiangmian
            "prod_088" -> R.drawable.caipin_hanshizhaji_lachaoniangao
            "prod_089" -> R.drawable.caipin_hanshizhaji_zicaibaofan
            "prod_090" -> R.drawable.caipin_hanshizhaji_zhishiregou
            "prod_091" -> R.drawable.caipin_hanshizhaji_yubingchuan
            "prod_092" -> R.drawable.caipin_hanshizhaji_hanshizhazhupai
            "prod_366" -> R.drawable.caipin_hanshizhaji_fengmihuangyouzhaji
            "prod_367" -> R.drawable.caipin_hanshizhaji_zhishizhaji
            "prod_368" -> R.drawable.caipin_hanshizhaji_jiangyouzhaji
            "prod_369" -> R.drawable.caipin_hanshizhaji_hanshilajiangzhaji
            "prod_370" -> R.drawable.caipin_hanshizhajiyuanweizhaji
            // 麦当劳
            "prod_166" -> R.drawable.caipin_maidanglao_hanbao
            "prod_167" -> R.drawable.caipin_maidanglao_zhaji
            "prod_168" -> R.drawable.caipin_maidanglao_shutiao
            "prod_169" -> R.drawable.caipin_maidanglao_jikuai
            "prod_170" -> R.drawable.caipin_maidanglao_jichi
            "prod_171" -> R.drawable.caipin_maidanglao_kele
            "prod_172" -> R.drawable.caipin_maidanglao_naixi
            "prod_173" -> R.drawable.caipin_maidanglao_shengdai
            "prod_174" -> R.drawable.caipin_maidanglao_pai
            "prod_175" -> R.drawable.caipin_maidanglao_hanbaotaocan
            "prod_176" -> R.drawable.caipin_maidanglao_zaocantaocan
            "prod_177" -> R.drawable.caipin_maidanglao_ertongtaocan
            "prod_178" -> R.drawable.caipin_maidanglao_maileji
            "prod_179" -> R.drawable.caipin_maidanglao_mailajichi
            "prod_180" -> R.drawable.caipin_maidanglao_yumibei
            "prod_401" -> R.drawable.caipin_maidanglao_zhaopaitaocana
            "prod_402" -> R.drawable.caipin_maidanglao_zhaopaitaocanb
            "prod_403" -> R.drawable.caipin_maidanglao_teseyinpin
            "prod_404" -> R.drawable.caipin_maidanglao_jingpinxiaoshi
            "prod_405" -> R.drawable.caipin_maidanglao_tesetianpin
            // 黄焖鸡米饭
            "prod_256" -> R.drawable.caipin_huangmenjimifan_huangmenji
            "prod_257" -> R.drawable.caipin_huangmenjimifan_jiroufan
            "prod_258" -> R.drawable.caipin_huangmenjimifan_niuroufan
            "prod_259" -> R.drawable.caipin_huangmenjimifan_paigufan
            "prod_260" -> R.drawable.caipin_huangmenjimifan_hongshaoroufan
            "prod_261" -> R.drawable.caipin_huangmenjimifan_gaijiaofan
            "prod_262" -> R.drawable.caipin_huangmenjimifan_chaofan
            "prod_263" -> R.drawable.caipin_huangmenjimifan_roujiamo
            "prod_264" -> R.drawable.caipin_huangmenjimifan_shaobing
            "prod_265" -> R.drawable.caipin_huangmenjimifan_jianbing
            "prod_266" -> R.drawable.caipin_huangmenjimifan_jidanguanbing
            "prod_267" -> R.drawable.caipin_huangmenjimifan_huangmenjimifantesecai1
            "prod_268" -> R.drawable.caipin_huangmenjimifan_huangmenjimifantesecai2
            "prod_269" -> R.drawable.caipin_huangmenjimifan_huangmenjimifantesecai3
            "prod_270" -> R.drawable.caipin_huangmenjimifan_huangmenjimifantesecai4
            "prod_431" -> R.drawable.caipin_huangmenjimifan_zhaopaitaocana
            "prod_432" -> R.drawable.caipin_huangmenjimifan_zhaopaitaocanb
            "prod_433" -> R.drawable.caipin_huangmenjimifan_teseyinpin
            "prod_434" -> R.drawable.caipin_huangmenjimifan_jingpinxiaoshi
            "prod_435" -> R.drawable.caipin_huangmenjimifan_tesetianpin
            else -> R.drawable.tongyong
        }
    }
}

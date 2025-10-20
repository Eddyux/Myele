# MyEle - 仿饿了么外卖APP

这是一个基于Android Jetpack Compose开发的仿饿了么外卖应用，用于AI操作和测试。

## 项目特点

- **无需登录**：所有功能本地运行，无需账号登录
- **无需联网**：所有数据存储在本地assets目录
- **固定数据**：使用预设的JSON数据，不需要动态更新
- **MVP架构**：严格遵循MVP设计模式，代码结构清晰
- **Material Design 3**：使用最新的Material 3设计规范

## 技术栈

- **语言**: Kotlin
- **UI框架**: Jetpack Compose
- **架构模式**: MVP (Model-View-Presenter)
- **导航**: Navigation Compose
- **数据解析**: Gson
- **最低SDK**: API 33 (Android 13)
- **目标SDK**: API 33

## 项目结构

```
app/src/main/
├── java/com/example/myele/
│   ├── data/                    # 数据层
│   │   └── DataRepository.kt    # 数据仓库，负责加载JSON数据
│   ├── model/                   # 数据模型
│   │   ├── User.kt             # 用户相关模型
│   │   ├── Restaurant.kt       # 餐厅模型
│   │   ├── Product.kt          # 商品模型
│   │   ├── Order.kt            # 订单模型
│   │   ├── Coupon.kt           # 优惠券模型
│   │   ├── Address.kt          # 地址模型
│   │   ├── Review.kt           # 评价模型
│   │   └── Task.kt             # 任务模型
│   ├── navigation/              # 导航相关
│   │   ├── Screen.kt           # 屏幕路由定义
│   │   └── NavGraph.kt         # 导航图配置
│   ├── ui/                      # UI层
│   │   ├── main/               # 主界面
│   │   │   └── MainScreen.kt   # 底部导航栏
│   │   ├── home/               # 首页模块
│   │   │   ├── HomeContract.kt # MVP契约接口
│   │   │   ├── HomePresenter.kt # Presenter层
│   │   │   └── HomeScreen.kt   # View层（UI）
│   │   ├── search/             # 搜索页面模块
│   │   │   ├── SearchContract.kt # MVP契约接口
│   │   │   ├── SearchPresenter.kt # Presenter层
│   │   │   └── SearchScreen.kt # View层（UI）
│   │   ├── searchresult/       # 搜索结果页面模块
│   │   │   ├── SearchResultContract.kt # MVP契约接口
│   │   │   ├── SearchResultPresenter.kt # Presenter层
│   │   │   └── SearchResultScreen.kt # View层（UI）
│   │   ├── takeout/            # 外卖页面模块
│   │   │   ├── TakeoutContract.kt # MVP契约接口
│   │   │   ├── TakeoutPresenter.kt # Presenter层
│   │   │   └── TakeoutScreen.kt # View层（UI）
│   │   ├── messages/           # 消息页面模块
│   │   │   └── MessagesScreen.kt # View层（UI）
│   │   ├── shoppingcart/       # 购物车页面模块
│   │   │   └── ShoppingCartScreen.kt # View层（UI）
│   │   ├── profile/            # 我的页面模块
│   │   │   └── ProfileScreen.kt # View层（UI）
│   │   ├── foodiecard/         # 超级吃货卡模块
│   │   │   ├── FoodieCardContract.kt # MVP契约接口
│   │   │   ├── FoodieCardPresenter.kt # Presenter层
│   │   │   └── FoodieCardScreen.kt # View层（UI）
│   │   ├── myorders/           # 我的订单模块
│   │   │   ├── MyOrdersContract.kt # MVP契约接口
│   │   │   ├── MyOrdersPresenter.kt # Presenter层
│   │   │   └── MyOrdersScreen.kt # View层（UI）
│   │   ├── orderdetail/        # 订单详情模块
│   │   │   └── OrderDetailScreen.kt # View层（UI）
│   │   ├── onlinechat/         # 在线聊天模块
│   │   │   └── OnlineChatScreen.kt # View层（UI）
│   │   ├── coupons/            # 红包卡券模块
│   │   │   └── CouponsScreen.kt # View层（UI）
│   │   ├── reviews/            # 评价中心模块
│   │   │   └── ReviewsScreen.kt # View层（UI）
│   │   ├── mybills/            # 我的账单模块
│   │   │   └── MyBillsScreen.kt # View层（UI）
│   │   ├── myaddresses/        # 我的地址模块
│   │   │   └── MyAddressesScreen.kt # View层（UI）
│   │   ├── checkout/           # 结算页面模块
│   │   │   └── CheckoutScreen.kt # View层（UI）
│   │   └── theme/              # 主题配置
│   └── MainActivity.kt          # 主Activity
└── assets/data/                 # JSON数据文件
    ├── users.json              # 用户数据
    ├── restaurants.json        # 餐厅数据
    ├── products.json           # 商品数据
    ├── orders.json             # 订单数据
    ├── coupons.json            # 优惠券数据
    ├── addresses.json          # 地址数据
    ├── reviews.json            # 评价数据
    ├── wallets.json            # 钱包数据
    ├── bills.json              # 账单数据
    ├── settings.json           # 设置数据
    ├── tasks.json              # 任务数据
    └── search_history.json     # 搜索历史
```

## 已完成的功能

### 首页 (Home)
- ✅ 顶部标签栏（常点、推荐、常用）
- ✅ 地址显示
- ✅ 搜索栏（可点击跳转到搜索页面）
- ✅ 广告横幅
- ✅ 服务入口图标（美食外卖、超市便利、学生福利等）
- ✅ 爆品团推荐区域
- ✅ 功能按钮（换一换、天天爆红包、减配送费、学生特价）
- ✅ 商家列表展示（评分、月售、配送时间、距离、优惠活动）
- ✅ 商家图片左对齐布局

### 搜索页面 (Search)
- ✅ 顶部搜索栏（带返回按钮）
- ✅ 搜索推荐（可点击跳转到搜索结果）
- ✅ 历史搜索（可清空，可点击跳转）
- ✅ 搜索发现（带标签）
- ✅ 专属好店推荐（带排名徽章）
- ✅ MVP架构实现

### 搜索结果页面 (SearchResult)
- ✅ 顶部搜索栏（显示关键词，带切换地址按钮）
- ✅ 排序选项（综合排序、销量优先、速度优先、筛选）
- ✅ 餐厅列表展示（图片、评分、销量、配送信息）
- ✅ 优惠活动标签
- ✅ 商品预览展示
- ✅ 底部红包提示横幅
- ✅ MVP架构实现

### 外卖页面 (Takeout)
- ✅ 顶部标题栏（带返回按钮和更多选项）
- ✅ 搜索栏
- ✅ 分类图标（精选、汉堡薯条、地方菜系等）
- ✅ 广告横幅
- ✅ 排序和筛选选项
- ✅ 功能标签（换一换、天天爆红包等）
- ✅ 餐厅列表（带优享大牌标签）
- ✅ MVP架构实现

### 消息页面 (Messages)
- ✅ 顶部标题栏（带清除和设置按钮）
- ✅ 平台消息板块
- ✅ 聊天动态列表
- ✅ 消息卡片展示（头像、标题、内容、时间）

### 购物车页面 (ShoppingCart)
- ✅ 顶部标题栏（显示地址和编辑按钮）
- ✅ 快捷入口（我常买、全能超市）
- ✅ 购物车商品列表（按店铺分组）
- ✅ 店铺级复选框（可整店选择/取消）
- ✅ 商品级复选框（可单选商品）
- ✅ 商品卡片（图片、名称、价格、数量控制）
- ✅ 无法购买商品提示
- ✅ 底部结算栏（全选、合计、一键结算）
- ✅ 结算按钮跳转到Checkout页面

### 我的页面 (Profile)
- ✅ 顶部个人信息区域（头像、姓名、手机号）
- ✅ 设置按钮
- ✅ 悦享俱乐部横幅
- ✅ 用户快捷功能（超级吃货卡、吃货豆、红包卡券）
- ✅ 我的订单板块（**全部**、待收货/使用、待评价、退款）
- ✅ 我的钱包板块（借钱、零钱、外卖红包、笔笔返现）
- ✅ 我的关注和常点的店
- ✅ 更多功能（我的地址、我的客服等）
- ✅ 你可能还喜欢推荐区域

### 结算页面 (Checkout)
- ✅ 顶部标题栏（确认订单）
- ✅ 配送方式选择（外卖配送/到店自取）
- ✅ 收货地址信息展示
- ✅ 配送时间选择（立即送出/预约配送）
- ✅ 订单商品列表
- ✅ 费用明细（商品总额、打包费、配送费、小计）
- ✅ 订单选项（备注、餐具、发票）
- ✅ 支付方式选择
- ✅ 底部提交订单按钮

### 底部导航栏
- ✅ 首页（第1个位置）
- ✅ 中间价格按钮（第2个位置，圆形蓝色按钮）
- ✅ 消息（第3个位置，带未读消息标记）
- ✅ 购物车（第4个位置）
- ✅ 我的（第5个位置）

## 待开发的页面

根据`开发要求.md`中定义的页面树，以下页面尚待开发：

- [x] 搜索页面 (Search) - 已完成
- [x] 搜索结果页面 (SearchResult) - 已完成
- [x] 外卖页面 (Takeout) - 已完成
- [x] 结算页面 (Checkout) - **已完成**
- [x] 消息页面 (Messages) - 已完成
- [x] 购物车 (ShoppingCart) - 已完成
- [x] 我的页面 (Profile) - 已完成
  - [x] 超级吃货卡 (FoodieCard) - **已完成**
  - [x] 我的订单 (MyOrders) - **已完成**
  - [x] 订单详情 (OrderDetail) - **已完成**
  - [x] 在线聊天 (OnlineChat) - **已完成**
  - [x] 红包卡券 (Coupons) - **已完成**
  - [x] 待评价/已评价 (Reviews) - **已完成**
  - [x] 我的账单 (MyBills) - **已完成**
  - [x] 我的地址 (MyAddresses) - **已完成**
  - [ ] 我的关注 (MyFollows)
  - [ ] 常点的店 (FrequentStores)
  - [ ] 我的客服 (CustomerService)
  - [ ] 设置 (Settings)
    - [ ] 支付设置 (PaymentSettings)
    - [ ] 消息通知设置 (NotificationSettings)
    - [ ] 我的信息 (MyInfo)
      - [ ] 更换手机号 (ChangePhone)

## 数据说明

所有数据存储在`app/src/main/assets/data/`目录下，采用JSON格式。数据围绕武汉洪山区华中师范大学周边设置：

- **收货地址**：均位于洪山区，家地址为华中师范大学元宝山公寓二期六栋
- **收货人**：统一为"于骁"
- **餐厅地址**：包含川香麻辣烫、老北京炸酱面、湘味轩、粤式早茶、韩式炸鸡等正餐店铺
- **饮品店铺**：瑞幸咖啡、茶百道、蜜雪冰城、星巴克、喜茶
- **商品**：各餐厅的特色菜品和饮品，包含规格选项
- **优惠券**：各类满减券、折扣券、免配送费券等

## 如何构建

1. 克隆项目到本地
2. 使用Android Studio打开项目
3. 等待Gradle同步完成
4. 运行`./gradlew assembleDebug`或直接在Android Studio中点击Run按钮

## 注意事项

1. **不要修改**`开发要求.md`文件
2. 遵循MVP设计模式，每个页面都应包含Contract、Presenter和View
3. UI参考截图位于`UIReference/`目录
4. 图标优先使用Material Icons，找不到合适的可用emoji代替
5. 禁止执行删除文件等危险操作

## 图片资源替换指南

### 如何替换图片（头像、商家图片等）

目前应用中的图片使用**占位符**显示（灰色背景+图标），如需替换为真实图片：

**1. 准备图片文件**
- 将图片文件放入 `app/src/main/res/drawable/` 目录
- 推荐格式：PNG、JPG、WebP
- 命名规范：使用小写字母和下划线，如 `user_avatar.png`、`restaurant_logo_1.png`

**2. 图片使用位置说明**

| 图片类型 | 当前代码位置 | 替换方法 |
|---------|------------|---------|
| **用户头像** | `ProfileScreen.kt:96-109` | 将 `Icon(Icons.Default.Person)` 替换为 `Image(painter = painterResource(R.drawable.user_avatar))` |
| **商家图片** | `HomeScreen.kt` 商家卡片<br>`SearchResultScreen.kt` 餐厅列表<br>`TakeoutScreen.kt` 餐厅列表 | 将占位符 `Box` 替换为 `Image(painter = painterResource(R.drawable.restaurant_xxx))` |
| **商品图片** | `ShoppingCartScreen.kt:274-287`<br>`CheckoutScreen.kt` 商品列表 | 将 `Icon(Icons.Default.Restaurant)` 替换为 `Image(painter = painterResource(R.drawable.product_xxx))` |
| **骑手头像** | `MessagesScreen.kt` 聊天列表 | 将占位符替换为 `Image(painter = painterResource(R.drawable.rider_xxx))` |

**3. 代码替换示例**

当前占位符代码：
```kotlin
Box(
    modifier = Modifier.size(60.dp).clip(CircleShape).background(Color.LightGray),
    contentAlignment = Alignment.Center
) {
    Icon(imageVector = Icons.Default.Person, contentDescription = "头像")
}
```

替换为真实图片：
```kotlin
Image(
    painter = painterResource(id = R.drawable.user_avatar),
    contentDescription = "用户头像",
    modifier = Modifier.size(60.dp).clip(CircleShape),
    contentScale = ContentScale.Crop
)
```

**4. 需要添加的导入**
```kotlin
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
```

**5. 图片资源目录结构建议**
```
app/src/main/res/drawable/
├── user_avatar.png          # 用户头像
├── restaurant_logo_1.png    # 川香麻辣烫
├── restaurant_logo_2.png    # 老北京炸酱面
├── restaurant_logo_3.png    # 湘味轩
├── product_chicken.png      # 荷叶烤鸡
├── product_duck.png         # 北京烤鸭
└── rider_avatar_1.png       # 骑手头像
```

**注意**：图片文件添加后需要重新构建项目（Build > Rebuild Project）

## 构建状态

✅ 最新构建：成功 (BUILD SUCCESSFUL)

构建时间：约21秒

## 更新日志

### 2025-10-20 (第六次更新 - 商家图片集成)
- ✅ 添加11张商家图片资源(川香麻辣烫、老北京炸酱面、湘味轩、粤式早茶、韩式炸鸡、瑞幸咖啡、茶百道、蜜雪冰城、星巴克、喜茶、用户头像)
- ✅ 创建ImageUtils工具类，根据餐厅名称自动匹配对应图片
- ✅ 创建RestaurantImage和UserAvatar可复用组件
- ✅ 在所有页面替换餐厅占位符图标为实际图片(HomeScreen、TakeoutScreen、SearchResultScreen、FoodieCardScreen、ProfileScreen、SearchScreen)
- ✅ 用户头像从占位符替换为实际图片
- ✅ 自动fallback机制：若无图片则显示默认图标
- ✅ 所有修改已通过构建测试(BUILD SUCCESSFUL)

### 2025-10-20 (第五次更新 - 底部导航栏优化)
- ✅ 优化底部导航栏显示逻辑，子页面不显示底部导航栏
- ✅ 仅在主页面(首页、消息、购物车、我的)显示底部导航栏
- ✅ Profile子页面导航时自动隐藏底部导航栏，提供更好的用户体验
- ✅ 所有修改已通过构建测试(BUILD SUCCESSFUL)

### 2025-10-20 (第四次更新 - Profile子页面全面实现)
- ✅ 完成超级吃货卡(FoodieCard)页面开发(MVP架构，红包套餐展示、商家筛选)
- ✅ 完成我的订单(MyOrders)页面开发(MVP架构，订单列表、订单筛选、月度支出)
- ✅ 完成订单详情(OrderDetail)页面开发(订单状态、商品信息、配送信息、联系骑手)
- ✅ 完成在线聊天(OnlineChat)页面开发(骑手聊天界面、快捷功能)
- ✅ 完成红包卡券(Coupons)页面开发(红包列表、优惠券分类、使用条件)
- ✅ 完成评价中心(Reviews)页面开发(待评价、已评价、评价奖励、商家回复)
- ✅ 完成我的账单(MyBills)页面开发(周账单、月账单、品类偏好、消费排行、场景偏好)
- ✅ 完成我的地址(MyAddresses)页面开发(地址列表、地址标签)
- ✅ Profile页面所有功能按钮添加导航跳转
- ✅ DataRepository添加get系列方法供MVP Presenter使用
- ✅ 所有修改已通过构建测试(BUILD SUCCESSFUL)

### 2025-10-19 (第三次更新 - 基于最新要求)
- ✅ 首页"美食外卖"按钮添加跳转到Takeout页面
- ✅ 购物车完善复选框功能（店铺级和商品级双层选择）
- ✅ 购物车"一键结算"按钮添加跳转到Checkout页面
- ✅ Profile页面添加个人信息头部（头像、姓名"于骁"、手机号）
- ✅ Profile页面添加设置按钮
- ✅ Profile我的订单板块添加"全部"选项
- ✅ 完成Checkout结算页面开发（配送方式、地址、商品、费用明细、支付方式等）
- ✅ 所有修改已通过构建测试

### 2025-10-19 (第二次更新)
- ✅ 完成SearchResult页面开发（搜索结果展示、排序筛选、商品预览）
- ✅ 完成Takeout页面开发（外卖分类、餐厅列表、优惠活动）
- ✅ 完成Messages页面开发（消息中心、平台消息、聊天动态）
- ✅ 完成ShoppingCart页面开发（购物车管理、商品数量控制、结算功能）
- ✅ 完成Profile页面开发（我的订单、钱包、关注店铺、更多功能）
- ✅ 所有新增页面均已添加到导航系统
- ✅ Build成功，所有页面可正常访问

### 2025-10-19 (第一次更新)
- ✅ 调整底部导航栏顺序：首页、价格图标(圆形按钮)、消息、购物车、我的
- ✅ 修改Home页面商家卡片布局，图片从右侧移到左侧
- ✅ 完成Search页面开发，包含搜索推荐、历史搜索、搜索发现、专属好店等功能
- ✅ 实现Search页面的MVP架构
- ✅ 添加Home页面搜索栏点击跳转到Search页面的功能

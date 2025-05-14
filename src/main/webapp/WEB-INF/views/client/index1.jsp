<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <meta content="width=device-width, initial-scale=1.0" name="viewport">
    <title>Index - Yummy Bootstrap Template</title>
    <meta name="description" content="">
    <meta name="keywords" content="">

    <!-- Favicons -->
    <link href="assets/img/favicon.png" rel="icon">
    <link href="assets/img/apple-touch-icon.png" rel="apple-touch-icon">

    <!-- Fonts -->
    <link href="https://fonts.googleapis.com" rel="preconnect">
    <link href="https://fonts.gstatic.com" rel="preconnect" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Roboto:ital,wght@0,100;0,300;0,400;0,500;0,700;0,900;1,100;1,300;1,400;1,500;1,700;1,900&family=Inter:wght@100;200;300;400;500;600;700;800;900&family=Amatic+SC:wght@400;700&display=swap" rel="stylesheet">

    <!-- Vendor CSS Files -->
    <link href="assets/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link href="assets/vendor/bootstrap-icons/bootstrap-icons.css" rel="stylesheet">
    <link href="assets/vendor/aos/aos.css" rel="stylesheet">
    <link href="assets/vendor/glightbox/css/glightbox.min.css" rel="stylesheet">
    <link href="assets/vendor/swiper/swiper-bundle.min.css" rel="stylesheet">

    <!-- Main CSS File -->
    <link href="assets/css/main.css" rel="stylesheet">

    <!-- =======================================================
    * Template Name: Yummy
    * Template URL: https://bootstrapmade.com/yummy-bootstrap-restaurant-website-template/
    * Updated: Aug 07 2024 with Bootstrap v5.3.3
    * Author: BootstrapMade.com
    * License: https://bootstrapmade.com/license/
    ======================================================== -->
</head>
<body class="index-page">

<header id="header" class="header d-flex align-items-center sticky-top">
    <div class="container position-relative d-flex align-items-center justify-content-between">

        <a href="index.jsp" class="logo d-flex align-items-center me-auto me-xl-0">
            <!-- Uncomment the line below if you also wish to use an image logo -->
            <!-- <img src="assets/img/logo.png" alt=""> -->
            <h1 class="sitename">Yummy</h1>
            <span>.</span>
        </a>

        <nav id="navmenu" class="navmenu">
            <ul>
                <li><a href="#hero" class="active">Trang chủ<br></a></li>
                <li><a href="#about">Về chúng tôi</a></li>
                <li><a href="#menu">Thực đơn</a></li>
                <li><a href="#events">Sự kiện</a></li>
                <li><a href="#chefs">Đầu bếp</a></li>
                <li><a href="#gallery">Trưng bày</a></li>
                <li class="dropdown"><a href="#"><span>Dropdown</span> <i class="bi bi-chevron-down toggle-dropdown"></i></a>
                    <ul>
                        <li><a href="#">Dropdown 1</a></li>
                        <li class="dropdown"><a href="#"><span>Deep Dropdown</span> <i class="bi bi-chevron-down toggle-dropdown"></i></a>
                            <ul>
                                <li><a href="#">Deep Dropdown 1</a></li>
                                <li><a href="#">Deep Dropdown 2</a></li>
                                <li><a href="#">Deep Dropdown 3</a></li>
                                <li><a href="#">Deep Dropdown 4</a></li>
                                <li><a href="#">Deep Dropdown 5</a></li>
                            </ul>
                        </li>
                        <li><a href="#">Dropdown 2</a></li>
                        <li><a href="#">Dropdown 3</a></li>
                        <li><a href="#">Dropdown 4</a></li>
                    </ul>
                </li>
                <li><a href="#contact">Liên hệ</a></li>
            </ul>
            <i class="mobile-nav-toggle d-xl-none bi bi-list"></i>
        </nav>

        <a class="btn-getstarted" href="#book-a-table">Đặt bàn</a>
        <a class="btn-getstarted" href="http://localhost:8080/crm/loginforadmin">Đăng nhập</a>

    </div>
</header>

<main class="main">

    <!-- Hero Section -->
    <section id="hero" class="hero section light-background">

        <div class="container">
            <div class="row gy-4 justify-content-center justify-content-lg-between">
                <div class="col-lg-5 order-2 order-lg-1 d-flex flex-column justify-content-center">
                    <h1 data-aos="fade-up">Thưởng Thức Đồ Ăn Ngon<br>Tốt Cho Sức Khỏe</h1>
                    <p data-aos="fade-up" data-aos-delay="100">Đảm bảo chất lượng phục vụ và món ăn đảm bảo cho khách hàng</p>
                    <div class="d-flex" data-aos="fade-up" data-aos-delay="200">
                        <a href="#book-a-table" class="btn-get-started">Đặt bàn ngay</a>
                        <a href="https://www.youtube.com/watch?v=Y7f98aduVJ8" class="glightbox btn-watch-video d-flex align-items-center"><i class="bi bi-play-circle"></i><span>Xem video</span></a>
                    </div>
                </div>
                <div class="col-lg-5 order-1 order-lg-2 hero-img" data-aos="zoom-out">
                    <img src="assets/img/hero-img.png" class="img-fluid animated" alt="">
                </div>
            </div>
        </div>

    </section><!-- /Hero Section -->

    <!-- About Section -->
    <section id="about" class="about section">

        <!-- Section Title -->
        <div class="container section-title" data-aos="fade-up">
            <h2>Về chúng tôi<br></h2>
            <p><span>Biết thêm</span> <span class="description-title">về chúng tôi</span></p>
        </div><!-- End Section Title -->

        <div class="container">

            <div class="row gy-4">
                <div class="col-lg-7" data-aos="fade-up" data-aos-delay="100">
                    <img src="assets/img/about.jpg" class="img-fluid mb-4" alt="">
                    <div class="book-a-table">
                        <h3>Đặt bàn ngay</h3>
                        <p>0971623584</p>
                    </div>
                </div>
                <div class="col-lg-5" data-aos="fade-up" data-aos-delay="250">
                    <div class="content ps-0 ps-lg-5">
                        <p class="fst-italic">
                            Nhà Hàng YUMMY tự hào là điểm đến lý tưởng cho những bữa tiệc gia đình,
                            buổi hẹn hò lãng mạn, hay các cuộc gặp gỡ bạn bè.
                        </p>
                        <ul>
                            <li><i class="bi bi-check-circle-fill"></i> <span>Thực đơn đa dạng, phong phú – từ các món ăn truyền thống đến các món hiện đại, phù hợp với mọi sở thích.</span></li>
                            <li><i class="bi bi-check-circle-fill"></i> <span>Không gian sang trọng, ấm cúng – thiết kế hiện đại nhưng vẫn mang lại cảm giác thoải mái, gần gũi.</span></li>
                            <li><i class="bi bi-check-circle-fill"></i> <span>Dịch vụ chuyên nghiệp – đội ngũ nhân viên tận tâm và luôn sẵn sàng phục vụ bạn mọi lúc.</span></li>
                        </ul>
                        <p>
                            Hãy gọi ngay để đặt bàn cho bữa tiệc tiếp theo của bạn tại Nhà Hàng YUMMY.
                            Chúng tôi sẽ chuẩn bị mọi thứ cho một bữa ăn tuyệt vời!
                        </p>

                        <div class="position-relative mt-4">
                            <img src="assets/img/about-2.jpg" class="img-fluid" alt="">
                            <a href="https://www.youtube.com/watch?v=Y7f98aduVJ8" class="glightbox pulsating-play-btn"></a>
                        </div>
                    </div>
                </div>
            </div>

        </div>

    </section><!-- /About Section -->

    <!-- Why Us Section -->
    <section id="why-us" class="why-us section light-background">

        <div class="container">

            <div class="row gy-4">

                <div class="col-lg-4" data-aos="fade-up" data-aos-delay="100">
                    <div class="why-box">
                        <h3>Tại sao chọn Yummy</h3>
                        <p>
                            Nhà hàng Yummy cam kết mang đến những món ăn ngon miệng với chất lượng hàng đầu.
                            Chúng tôi luôn tìm cách mang đến cho bạn những trải nghiệm ẩm thực tuyệt vời nhất với không gian thoải mái và dịch vụ chuyên nghiệp.
                        </p>
                        <div class="text-center">
                            <a href="#" class="more-btn"><span>Đọc thêm</span> <i class="bi bi-chevron-right"></i></a>
                        </div>
                    </div>
                </div><!-- End Why Box -->

                <div class="col-lg-8 d-flex align-items-stretch">
                    <div class="row gy-4" data-aos="fade-up" data-aos-delay="200">

                        <div class="col-xl-4">
                            <div class="icon-box d-flex flex-column justify-content-center align-items-center">
                                <i class="bi bi-clipboard-data"></i>
                                <h4>Chất lượng thực phẩm đảm bảo</h4>
                                <p>Chúng tôi sử dụng nguyên liệu tươi ngon và an toàn nhất.</p>
                            </div>
                        </div><!-- End Icon Box -->

                        <div class="col-xl-4" data-aos="fade-up" data-aos-delay="300">
                            <div class="icon-box d-flex flex-column justify-content-center align-items-center">
                                <i class="bi bi-gem"></i>
                                <h4>Thực đơn phong phú</h4>
                                <p>Mỗi món ăn đều được chế biến tinh tế, đa dạng để phù hợp với tất cả khẩu vị.</p>
                            </div>
                        </div><!-- End Icon Box -->

                        <div class="col-xl-4" data-aos="fade-up" data-aos-delay="400">
                            <div class="icon-box d-flex flex-column justify-content-center align-items-center">
                                <i class="bi bi-inboxes"></i>
                                <h4>Không gian sang trọng</h4>
                                <p>Một không gian lý tưởng để thưởng thức bữa ăn cùng gia đình và bạn bè.</p>
                            </div>
                        </div><!-- End Icon Box -->

                    </div>
                </div>

            </div>

        </div>

    </section><!-- /Why Us Section -->

    <!-- Stats Section -->
    <section id="stats" class="stats section dark-background">

        <img src="assets/img/stats-bg.jpg" alt="" data-aos="fade-in">

        <div class="container position-relative" data-aos="fade-up" data-aos-delay="100">

            <div class="row gy-4">

                <div class="col-lg-3 col-md-6">
                    <div class="stats-item text-center w-100 h-100">
                        <span data-purecounter-start="0" data-purecounter-end="232" data-purecounter-duration="1" class="purecounter"></span>
                        <p>Khách hàng</p>
                    </div>
                </div><!-- End Stats Item -->

                <div class="col-lg-3 col-md-6">
                    <div class="stats-item text-center w-100 h-100">
                        <span data-purecounter-start="0" data-purecounter-end="15" data-purecounter-duration="1" class="purecounter"></span>
                        <p>Món ăn</p>
                    </div>
                </div><!-- End Stats Item -->

                <div class="col-lg-3 col-md-6">
                    <div class="stats-item text-center w-100 h-100">
                        <span data-purecounter-start="0" data-purecounter-end="1453" data-purecounter-duration="1" class="purecounter"></span>
                        <p>Số giờ hỗ trợ</p>
                    </div>
                </div><!-- End Stats Item -->

                <div class="col-lg-3 col-md-6">
                    <div class="stats-item text-center w-100 h-100">
                        <span data-purecounter-start="0" data-purecounter-end="32" data-purecounter-duration="1" class="purecounter"></span>
                        <p>Nhân viên</p>
                    </div>
                </div><!-- End Stats Item -->

            </div>

        </div>

    </section><!-- /Stats Section -->

    <!-- Menu Section -->
    <section id="menu" class="menu section">

        <!-- Section Title -->
        <div class="container section-title" data-aos="fade-up">
            <h2>Thực đơn của chúng tôi</h2>
            <p><span>Xem thực đơn</span> <span class="description-title">Nhà hàng Yummy</span></p>
        </div><!-- End Section Title -->

        <div class="container">

            <ul class="nav nav-tabs d-flex justify-content-center" data-aos="fade-up" data-aos-delay="100">

                <li class="nav-item">
                    <a class="nav-link active show" data-bs-toggle="tab" data-bs-target="#menu-starters">
                        <h4>Gợi ý</h4>
                    </a>
                </li><!-- End tab nav item -->

                <li class="nav-item">
                    <a class="nav-link" data-bs-toggle="tab" data-bs-target="#menu-breakfast">
                        <h4>Bữa sáng</h4>
                    </a><!-- End tab nav item -->

                </li>
                <li class="nav-item">
                    <a class="nav-link" data-bs-toggle="tab" data-bs-target="#menu-lunch">
                        <h4>Bữa trưa</h4>
                    </a>
                </li><!-- End tab nav item -->

                <li class="nav-item">
                    <a class="nav-link" data-bs-toggle="tab" data-bs-target="#menu-dinner">
                        <h4>Bữa tối</h4>
                    </a>
                </li><!-- End tab nav item -->

            </ul>

            <div class="tab-content" data-aos="fade-up" data-aos-delay="200">

                <div class="tab-pane fade active show" id="menu-starters">

                    <div class="tab-header text-center">
                        <p>Thực đơn</p>
                        <h3>Gợi ý</h3>
                    </div>

                    <div class="row gy-5">
                        <%@ page import="java.util.List" %>
                        <%@ page import="org.datban.webjava.models.Food" %>
                        <%
                            List<Food> list = (List<Food>) request.getAttribute("foods");
                            if (list == null || list.isEmpty()) {
                        %>
                        <p style="color: red;">Không có món ăn gợi ý nào để hiển thị.</p>
                        <%
                        } else {
                            for (Food f : list) {
                        %>
                        <div class="col-lg-4 menu-item" onclick="addDish('<%= f.getName() %>')">
                            <a href="<%= f.getImageUrl() %>" class="glightbox">
                                <img src="<%= f.getImageUrl() %>" class="menu-img img-fluid" alt="">
                            </a>
                            <h4><%= f.getName() %></h4>
                            <p class="ingredients"><%= f.getDescription() %></p>
                            <p class="price"><%= f.getPrice() %></p>
                        </div>
                        <%
                                }
                            }
                        %>
                    </div>
                </div><!-- End Starter Menu Content -->

                <div class="tab-pane fade" id="menu-breakfast">

                    <div class="tab-header text-center">
                        <p>Thực đơn</p>
                        <h3>Bữa sáng</h3>
                    </div>
                    <!-- Thêm phần hiển thị list foodsbr (món ăn sáng) -->
                    <div class="row gy-5">
                        <%
                            List<Food> listbr = (List<Food>) request.getAttribute("foodsbr");
                            if (listbr == null || listbr.isEmpty()) {
                        %>
                        <p style="color: red;">Không có món ăn gợi ý nào để hiển thị.</p>
                        <%
                        } else {
                            for (Food f : listbr) {
                        %>
                        <div class="col-lg-4 menu-item" onclick="addDish('<%= f.getName() %>')">
                            <a href="<%= f.getImageUrl() %>" class="glightbox">
                                <img src="<%= f.getImageUrl() %>" class="menu-img img-fluid" alt="">
                            </a>
                            <h4><%= f.getName() %></h4>
                            <p class="ingredients"><%= f.getDescription() %></p>
                            <p class="price"><%= f.getPrice() %></p>
                        </div>
                        <%
                                }
                            }
                        %>
                    </div>

                </div><!-- End Breakfast Menu Content -->

                <div class="tab-pane fade" id="menu-lunch">

                    <div class="tab-header text-center">
                        <p>Thực đơn</p>
                        <h3>Bữa trưa</h3>
                        <div class="row gy-5">
                            <%
                                List<Food> listlu = (List<Food>) request.getAttribute("foodslu");
                                if (listlu == null || listlu.isEmpty()) {
                            %>
                            <p style="color: red;">Không có món ăn gợi ý nào để hiển thị.</p>
                            <%
                            } else {
                                for (Food f : listlu) {
                            %>
                            <div class="col-lg-4 menu-item" onclick="addDish('<%= f.getName() %>')">
                                <a href="<%= f.getImageUrl() %>" class="glightbox">
                                    <img src="<%= f.getImageUrl() %>" class="menu-img img-fluid" alt="">
                                </a>
                                <h4><%= f.getName() %></h4>
                                <p class="ingredients"><%= f.getDescription() %></p>
                                <p class="price"><%= f.getPrice() %></p>
                            </div>
                            <%
                                    }
                                }
                            %>
                        </div>

                    </div>
                </div><!-- End Lunch Menu Content -->

                <div class="tab-pane fade" id="menu-dinner">

                    <div class="tab-header text-center">
                        <p>Thực đơn</p>
                        <h3>Bữa tối</h3>
                        <div class="row gy-5">

                            <%
                                List<Food> listdn = (List<Food>) request.getAttribute("foodsdn");
                                if (listdn == null || listdn.isEmpty()) {
                            %>
                            <p style="color: red;">Không có món ăn gợi ý nào để hiển thị.</p>
                            <%
                            } else {
                                for (Food f : listdn) {
                            %>
                            <div class="col-lg-4 menu-item" onclick="addDish('<%= f.getName() %>')">
                                <a href="<%= f.getImageUrl() %>" class="glightbox">
                                    <img src="<%= f.getImageUrl() %>" class="menu-img img-fluid" alt="">
                                </a>
                                <h4><%= f.getName() %></h4>
                                <p class="ingredients"><%= f.getDescription() %></p>
                                <p class="price"><%= f.getPrice() %></p>
                            </div>
                            <%
                                    }
                                }
                            %>
                        </div>

                    </div>
                </div><!-- End Dinner Menu Content -->

            </div>

        </div>

    </section><!-- /Menu Section -->

    <!-- Testimonials Section -->
    <section id="testimonials" class="testimonials section light-background">

        <!-- Section Title -->
        <div class="container section-title" data-aos="fade-up">
            <h2>Đánh giá từ khách hàng</h2>
            <p>Khách hàng <span class="description-title">Nói gì về chúng tôi</span></p>
        </div><!-- End Section Title -->

        <div class="container" data-aos="fade-up" data-aos-delay="100">

            <div class="swiper init-swiper">
                <script type="application/json" class="swiper-config">
                    {
                        "loop": true,
                        "speed": 600,
                        "autoplay": {
                            "delay": 5000
                        },
                        "slidesPerView": "auto",
                        "pagination": {
                            "el": ".swiper-pagination",
                            "type": "bullets",
                            "clickable": true
                        }
                    }
                </script>
                <div class="swiper-wrapper">

                    <div class="swiper-slide">
                        <div class="testimonial-item">
                            <div class="row gy-4 justify-content-center">
                                <div class="col-lg-6">
                                    <div class="testimonial-content">
                                        <p>
                                            <i class="bi bi-quote quote-icon-left"></i>
                                            <span>Proin iaculis purus consequat sem cure digni ssim donec porttitora entum suscipit rhoncus. Accusantium quam, ultricies eget id, aliquam eget nibh et. Maecen aliquam, risus at semper.</span>
                                            <i class="bi bi-quote quote-icon-right"></i>
                                        </p>
                                        <h3>Saul Goodman</h3>
                                        <h4>Ceo &amp; Founder</h4>
                                        <div class="stars">
                                            <i class="bi bi-star-fill"></i><i class="bi bi-star-fill"></i><i class="bi bi-star-fill"></i><i class="bi bi-star-fill"></i><i class="bi bi-star-fill"></i>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-lg-2 text-center">
                                    <img src="assets/img/testimonials/testimonials-1.jpg" class="img-fluid testimonial-img" alt="">
                                </div>
                            </div>
                        </div>
                    </div><!-- End testimonial item -->

                    <div class="swiper-slide">
                        <div class="testimonial-item">
                            <div class="row gy-4 justify-content-center">
                                <div class="col-lg-6">
                                    <div class="testimonial-content">
                                        <p>
                                            <i class="bi bi-quote quote-icon-left"></i>
                                            <span>Export tempor illum tamen malis malis eram quae irure esse labore quem cillum quid cillum eram malis quorum velit fore eram velit sunt aliqua noster fugiat irure amet legam anim culpa.</span>
                                            <i class="bi bi-quote quote-icon-right"></i>
                                        </p>
                                        <h3>Sara Wilsson</h3>
                                        <h4>Designer</h4>
                                        <div class="stars">
                                            <i class="bi bi-star-fill"></i><i class="bi bi-star-fill"></i><i class="bi bi-star-fill"></i><i class="bi bi-star-fill"></i><i class="bi bi-star-fill"></i>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-lg-2 text-center">
                                    <img src="assets/img/testimonials/testimonials-2.jpg" class="img-fluid testimonial-img" alt="">
                                </div>
                            </div>
                        </div>
                    </div><!-- End testimonial item -->

                    <div class="swiper-slide">
                        <div class="testimonial-item">
                            <div class="row gy-4 justify-content-center">
                                <div class="col-lg-6">
                                    <div class="testimonial-content">
                                        <p>
                                            <i class="bi bi-quote quote-icon-left"></i>
                                            <span>Enim nisi quem export duis labore cillum quae magna enim sint quorum nulla quem veniam duis minim tempor labore quem eram duis noster aute amet eram fore quis sint minim.</span>
                                            <i class="bi bi-quote quote-icon-right"></i>
                                        </p>
                                        <h3>Jena Karlis</h3>
                                        <h4>Store Owner</h4>
                                        <div class="stars">
                                            <i class="bi bi-star-fill"></i><i class="bi bi-star-fill"></i><i class="bi bi-star-fill"></i><i class="bi bi-star-fill"></i><i class="bi bi-star-fill"></i>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-lg-2 text-center">
                                    <img src="assets/img/testimonials/testimonials-3.jpg" class="img-fluid testimonial-img" alt="">
                                </div>
                            </div>
                        </div>
                    </div><!-- End testimonial item -->

                    <div class="swiper-slide">
                        <div class="testimonial-item">
                            <div class="row gy-4 justify-content-center">
                                <div class="col-lg-6">
                                    <div class="testimonial-content">
                                        <p>
                                            <i class="bi bi-quote quote-icon-left"></i>
                                            <span>Fugiat enim eram quae cillum dolore dolor amet nulla culpa multos export minim fugiat minim velit minim dolor enim duis veniam ipsum anim magna sunt elit fore quem dolore labore illum veniam.</span>
                                            <i class="bi bi-quote quote-icon-right"></i>
                                        </p>
                                        <h3>John Larson</h3>
                                        <h4>Entrepreneur</h4>
                                        <div class="stars">
                                            <i class="bi bi-star-fill"></i><i class="bi bi-star-fill"></i><i class="bi bi-star-fill"></i><i class="bi bi-star-fill"></i><i class="bi bi-star-fill"></i>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-lg-2 text-center">
                                    <img src="assets/img/testimonials/testimonials-4.jpg" class="img-fluid testimonial-img" alt="">
                                </div>
                            </div>
                        </div>
                    </div><!-- End testimonial item -->

                </div>
                <div class="swiper-pagination"></div>
            </div>

        </div>

    </section><!-- /Testimonials Section -->

    <!-- Events Section -->
    <section id="events" class="events section">

        <div class="container-fluid" data-aos="fade-up" data-aos-delay="100">

            <div class="swiper init-swiper">
                <script type="application/json" class="swiper-config">
                    {
                        "loop": true,
                        "speed": 600,
                        "autoplay": {
                            "delay": 5000
                        },
                        "slidesPerView": "auto",
                        "pagination": {
                            "el": ".swiper-pagination",
                            "type": "bullets",
                            "clickable": true
                        },
                        "breakpoints": {
                            "320": {
                                "slidesPerView": 1,
                                "spaceBetween": 40
                            },
                            "1200": {
                                "slidesPerView": 3,
                                "spaceBetween": 1
                            }
                        }
                    }
                </script>
                <div class="swiper-wrapper">

                    <div class="swiper-slide event-item d-flex flex-column justify-content-end" style="background-image: url(assets/img/events-1.jpg)">
                        <h3>Tiệc Tùy Chỉnh</h3>
                        <div class="price align-self-start">$99</div>
                        <p class="description">
                            Tổ chức tiệc theo yêu cầu với các món ăn và trang trí theo sở thích cá nhân. Chúng tôi sẽ giúp bạn tạo ra bầu không khí tuyệt vời nhất cho ngày đặc biệt.
                        </p>
                    </div><!-- End Event item -->

                    <div class="swiper-slide event-item d-flex flex-column justify-content-end" style="background-image: url(assets/img/events-2.jpg)">
                        <h3>Tiệc Riêng Tư</h3>
                        <div class="price align-self-start">$289</div>
                        <p class="description">
                            Dịch vụ tổ chức tiệc riêng, không gian dành riêng cho nhóm nhỏ, nơi bạn có thể thưởng thức bữa ăn ngon mà không bị làm phiền.
                        </p>
                    </div><!-- End Event item -->

                    <div class="swiper-slide event-item d-flex flex-column justify-content-end" style="background-image: url(assets/img/events-3.jpg)">
                        <h3>Tiệc Sinh Nhật</h3>
                        <div class="price align-self-start">$499</div>
                        <p class="description">
                            Tổ chức tiệc sinh nhật hoàn hảo với thực đơn đặc biệt, trang trí sinh động và không gian tuyệt vời. Chúng tôi sẽ chăm sóc tất cả mọi thứ để bạn có thể tận hưởng ngày vui trọn vẹn!
                        </p>
                    </div><!-- End Event item -->

                    <div class="swiper-slide event-item d-flex flex-column justify-content-end" style="background-image: url(assets/img/events-4.jpg)">
                        <h3>Tiệc Cưới</h3>
                        <div class="price align-self-start">$899</div>
                        <p class="description">
                            Tổ chức tiệc cưới trọn gói với không gian trang trí lãng mạn, thực đơn món ăn tinh tế và dịch vụ chu đáo.
                        </p>
                    </div><!-- End Event item -->

                </div>
                <div class="swiper-pagination"></div>
            </div>

        </div>

    </section><!-- /Events Section -->

    <!-- Chefs Section -->
    <section id="chefs" class="chefs section">

        <!-- Section Title -->
        <div class="container section-title" data-aos="fade-up">
            <h2>Đầu bếp</h2>
            <p><span>Đội ngũ đầu bếp</span> <span class="description-title">Của nhà hàng<br></span></p>
        </div><!-- End Section Title -->

        <div class="container">

            <div class="row gy-4">

                <div class="col-lg-4 d-flex align-items-stretch" data-aos="fade-up" data-aos-delay="100">
                    <div class="team-member">
                        <div class="member-img">
                            <img src="assets/img/chefs/chefs-1.jpg" class="img-fluid" alt="">
                            <div class="social">
                                <a href=""><i class="bi bi-twitter-x"></i></a>
                                <a href=""><i class="bi bi-facebook"></i></a>
                                <a href=""><i class="bi bi-instagram"></i></a>
                                <a href=""><i class="bi bi-linkedin"></i></a>
                            </div>
                        </div>
                        <div class="member-info">
                            <h4>Walter White</h4>
                            <span>Master Chef</span>
                            <p>Với kinh nghiệm lâu năm trong ngành ẩm thực, Walter White luôn sáng tạo và mang đến những món ăn độc đáo, đậm đà hương vị.</p>
                        </div>
                    </div>
                </div><!-- End Chef Team Member -->

                <div class="col-lg-4 d-flex align-items-stretch" data-aos="fade-up" data-aos-delay="200">
                    <div class="team-member">
                        <div class="member-img">
                            <img src="assets/img/chefs/chefs-2.jpg" class="img-fluid" alt="">
                            <div class="social">
                                <a href=""><i class="bi bi-twitter-x"></i></a>
                                <a href=""><i class="bi bi-facebook"></i></a>
                                <a href=""><i class="bi bi-instagram"></i></a>
                                <a href=""><i class="bi bi-linkedin"></i></a>
                            </div>
                        </div>
                        <div class="member-info">
                            <h4>Sarah Jhonson</h4>
                            <span>Patissier</span>
                            <p>Sarah là chuyên gia làm bánh tại nhà hàng. Cô ấy mang đến những chiếc bánh ngọt tuyệt vời, từ các món truyền thống đến hiện đại, luôn đảm bảo chất lượng và hương vị hoàn hảo.</p>
                        </div>
                    </div>
                </div><!-- End Chef Team Member -->

                <div class="col-lg-4 d-flex align-items-stretch" data-aos="fade-up" data-aos-delay="300">
                    <div class="team-member">
                        <div class="member-img">
                            <img src="assets/img/chefs/chefs-3.jpg" class="img-fluid" alt="">
                            <div class="social">
                                <a href=""><i class="bi bi-twitter-x"></i></a>
                                <a href=""><i class="bi bi-facebook"></i></a>
                                <a href=""><i class="bi bi-instagram"></i></a>
                                <a href=""><i class="bi bi-linkedin"></i></a>
                            </div>
                        </div>
                        <div class="member-info">
                            <h4>William Anderson</h4>
                            <span>Cook</span>
                            <p>William Anderson là đầu bếp với khả năng sáng tạo cao, luôn nỗ lực mang đến những món ăn vừa ngon vừa đẹp mắt.</p>
                        </div>
                    </div>
                </div><!-- End Chef Team Member -->

            </div>

        </div>

    </section><!-- /Chefs Section -->

    <!-- Book A Table Section -->
    <section id="book-a-table" class="book-a-table section">

        <!-- Section Title -->
        <div class="container section-title" data-aos="fade-up">
            <h2>Đặt bàn ngay</h2>
            <p><span>Hãy điền thông tin</span> <span class="description-title">để thưởng thức tại YUMMY<br></span></p>
        </div><!-- End Section Title -->

        <div class="container">

            <div class="row g-0" data-aos="fade-up" data-aos-delay="100">

                <div class="col-lg-4 reservation-img" style="background-image: url(assets/img/reservation.jpg);"></div>

                <div class="col-lg-8 d-flex align-items-center reservation-form-bg" data-aos="fade-up" data-aos-delay="200">
                    <form action="forms/book-a-table.php" method="post" \role="form" class="php-email-form">
                        <div class="row gy-4">
                            <div class="col-lg-4 col-md-6">
                                <input type="text" name="name" class="form-control" id="name" placeholder="Tên của bạn" required="">
                            </div>
                            <div class="col-lg-4 col-md-6">
                                <input type="email" class="form-control" name="email" id="email" placeholder="Email của bạn" required="">
                            </div>
                            <div class="col-lg-4 col-md-6">
                                <input type="text" class="form-control" name="phone" id="phone" placeholder="Số điện thoại" required="">
                            </div>
                            <div class="col-lg-4 col-md-6">
                                <input type="date" name="date" class="form-control" id="date" placeholder="Ngày đặt" required="">
                            </div>
                            <div class="col-lg-4 col-md-6">
                                <input type="time" class="form-control" name="time" id="time" placeholder="Thời giang" required="">
                            </div>
                            <div class="col-lg-4 col-md-6">
                                <input type="number" class="form-control" name="people" id="people" placeholder="# số người" required="">
                            </div>
                        </div>

                        <div class="form-group mt-3">
                            <textarea class="form-control" name="message" rows="5" id="orderTextArea" placeholder="Vui lòng chọn món trên thực đơn!"></textarea>
                        </div>

                        <div class="text-center mt-3">
                            <div class="loading">Loading</div>
                            <div class="error-message"></div>
                            <div class="sent-message">Bạn đã đặt bàn thành công. Chúng tôi sẽ gửi email để xác minh. Xin cảm ơn!</div>
                            <button type="submit">Đặt bàn</button>
                        </div>
                    </form>
                </div><!-- End Reservation Form -->

            </div>

        </div>

    </section><!-- /Book A Table Section -->

    <!-- Gallery Section -->
    <section id="gallery" class="gallery section light-background">

        <!-- Section Title -->
        <div class="container section-title" data-aos="fade-up">
            <h2>Trưng bày</h2>
            <p><span>Xem</span> <span class="description-title">Phòng trưng bày</span></p>
        </div><!-- End Section Title -->

        <div class="container" data-aos="fade-up" data-aos-delay="100">

            <div class="swiper init-swiper">
                <script type="application/json" class="swiper-config">
                    {
                        "loop": true,
                        "speed": 600,
                        "autoplay": {
                            "delay": 5000
                        },
                        "slidesPerView": "auto",
                        "centeredSlides": true,
                        "pagination": {
                            "el": ".swiper-pagination",
                            "type": "bullets",
                            "clickable": true
                        },
                        "breakpoints": {
                            "320": {
                                "slidesPerView": 1,
                                "spaceBetween": 0
                            },
                            "768": {
                                "slidesPerView": 3,
                                "spaceBetween": 20
                            },
                            "1200": {
                                "slidesPerView": 5,
                                "spaceBetween": 20
                            }
                        }
                    }
                </script>
                <div class="swiper-wrapper align-items-center">
                    <div class="swiper-slide"><a class="glightbox" data-gallery="images-gallery" href="assets/img/gallery/gallery-1.jpg"><img src="assets/img/gallery/gallery-1.jpg" class="img-fluid" alt=""></a></div>
                    <div class="swiper-slide"><a class="glightbox" data-gallery="images-gallery" href="assets/img/gallery/gallery-2.jpg"><img src="assets/img/gallery/gallery-2.jpg" class="img-fluid" alt=""></a></div>
                    <div class="swiper-slide"><a class="glightbox" data-gallery="images-gallery" href="assets/img/gallery/gallery-3.jpg"><img src="assets/img/gallery/gallery-3.jpg" class="img-fluid" alt=""></a></div>
                    <div class="swiper-slide"><a class="glightbox" data-gallery="images-gallery" href="assets/img/gallery/gallery-4.jpg"><img src="assets/img/gallery/gallery-4.jpg" class="img-fluid" alt=""></a></div>
                    <div class="swiper-slide"><a class="glightbox" data-gallery="images-gallery" href="assets/img/gallery/gallery-5.jpg"><img src="assets/img/gallery/gallery-5.jpg" class="img-fluid" alt=""></a></div>
                    <div class="swiper-slide"><a class="glightbox" data-gallery="images-gallery" href="assets/img/gallery/gallery-6.jpg"><img src="assets/img/gallery/gallery-6.jpg" class="img-fluid" alt=""></a></div>
                    <div class="swiper-slide"><a class="glightbox" data-gallery="images-gallery" href="assets/img/gallery/gallery-7.jpg"><img src="assets/img/gallery/gallery-7.jpg" class="img-fluid" alt=""></a></div>
                    <div class="swiper-slide"><a class="glightbox" data-gallery="images-gallery" href="assets/img/gallery/gallery-8.jpg"><img src="assets/img/gallery/gallery-8.jpg" class="img-fluid" alt=""></a></div>
                </div>
                <div class="swiper-pagination"></div>
            </div>

        </div>

    </section><!-- /Gallery Section -->

    <!-- Contact Section -->
    <section id="contact" class="contact section">

        <!-- Section Title -->
        <div class="container section-title" data-aos="fade-up">
            <h2>Liên hệ</h2>
            <p><span>Nếu bạn vần giúp đỡ?</span> <span class="description-title">Liên hệ với chúng tôi</span></p>
        </div><!-- End Section Title -->

        <div class="container" data-aos="fade-up" data-aos-delay="100">

            <div class="mb-5">
                <iframe style="width: 100%; height: 400px;" src="https://www.google.com/maps/embed?pb=!1m14!1m8!1m3!1d12097.433213460943!2d-74.0062269!3d40.7101282!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x0%3A0xb89d1fe6bc499443!2sDowntown+Conference+Center!5e0!3m2!1smk!2sbg!4v1539943755621" frameborder="0" allowfullscreen=""></iframe>
            </div><!-- End Google Maps -->

            <div class="row gy-4">

                <div class="col-md-6">
                    <div class="info-item d-flex align-items-center" data-aos="fade-up" data-aos-delay="200">
                        <i class="icon bi bi-geo-alt flex-shrink-0"></i>
                        <div>
                            <h3>Địa chỉ</h3>
                            <p>A108 Adam Street, New York, NY 535022</p>
                        </div>
                    </div>
                </div><!-- End Info Item -->

                <div class="col-md-6">
                    <div class="info-item d-flex align-items-center" data-aos="fade-up" data-aos-delay="300">
                        <i class="icon bi bi-telephone flex-shrink-0"></i>
                        <div>
                            <h3>Liên hệ</h3>
                            <p>0971623584</p>
                        </div>
                    </div>
                </div><!-- End Info Item -->

                <div class="col-md-6">
                    <div class="info-item d-flex align-items-center" data-aos="fade-up" data-aos-delay="400">
                        <i class="icon bi bi-envelope flex-shrink-0"></i>
                        <div>
                            <h3>Email</h3>
                            <p>phongdh1403@gmail.com</p>
                        </div>
                    </div>
                </div><!-- End Info Item -->

                <div class="col-md-6">
                    <div class="info-item d-flex align-items-center" data-aos="fade-up" data-aos-delay="500">
                        <i class="icon bi bi-clock flex-shrink-0"></i>
                        <div>
                            <h3>Thời gian mở cửa<br></h3>
                            <p><strong>Thứ hai-Thứ bảy:</strong> 11AM - 23PM; <strong>Chủ nhật:</strong> Đóng cửa</p>
                        </div>
                    </div>
                </div><!-- End Info Item -->

            </div>

            <form action="forms/contact.php" method="post" class="php-email-form" data-aos="fade-up" data-aos-delay="600">
                <div class="row gy-4">

                    <div class="col-md-6">
                        <input type="text" name="name" class="form-control" placeholder="Your Name" required="">
                    </div>

                    <div class="col-md-6 ">
                        <input type="email" class="form-control" name="email" placeholder="Your Email" required="">
                    </div>

                    <div class="col-md-12">
                        <input type="text" class="form-control" name="subject" placeholder="Subject" required="">
                    </div>

                    <div class="col-md-12">
                        <textarea class="form-control" name="message" rows="6" placeholder="Message" required=""></textarea>
                    </div>

                    <div class="col-md-12 text-center">
                        <div class="loading">Loading</div>
                        <div class="error-message"></div>
                        <div class="sent-message">Đánh giá của bạn đã được gửi đi. Cảm ơn!</div>

                        <button type="submit">Gửi đánh giá</button>
                    </div>

                </div>
            </form><!-- End Contact Form -->

        </div>

    </section><!-- /Contact Section -->

</main>

<footer id="footer" class="footer dark-background">

    <div class="container">
        <div class="row gy-3">
            <div class="col-lg-3 col-md-6 d-flex">
                <i class="bi bi-geo-alt icon"></i>
                <div class="address">
                    <h4>Địa chỉ</h4>
                    <p>A108 Adam Street</p>
                    <p>New York, NY 535022</p>
                    <p></p>
                </div>

            </div>

            <div class="col-lg-3 col-md-6 d-flex">
                <i class="bi bi-telephone icon"></i>
                <div>
                    <h4>Liên hệ</h4>
                    <p>
                        <strong>Phone:</strong> <span>+1 5589 55488 55</span><br>
                        <strong>Email:</strong> <span>info@example.com</span><br>
                    </p>
                </div>
            </div>

            <div class="col-lg-3 col-md-6 d-flex">
                <i class="bi bi-clock icon"></i>
                <div>
                    <h4>Giờ mở cửa</h4>
                    <p>
                        <strong>Thứ hai-Thứ bảy:</strong> <span>11AM - 23PM</span><br>
                        <strong>Chủ nhật:</strong>: <span>Đóng cửa</span>
                    </p>
                </div>
            </div>

            <div class="col-lg-3 col-md-6">
                <h4>Theo dõi chúng tôi</h4>
                <div class="social-links d-flex">
                    <a href="#" class="twitter"><i class="bi bi-twitter-x"></i></a>
                    <a href="#" class="facebook"><i class="bi bi-facebook"></i></a>
                    <a href="#" class="instagram"><i class="bi bi-instagram"></i></a>
                    <a href="#" class="linkedin"><i class="bi bi-linkedin"></i></a>
                </div>
            </div>

        </div>
    </div>

    <div class="container copyright text-center mt-4">
        <p>© <span>Copyright</span> <strong class="px-1 sitename">Yummy</strong> <span>All Rights Reserved</span></p>
        <div class="credits">
            <!-- All the links in the footer should remain intact. -->
            <!-- You can delete the links only if you've purchased the pro version. -->
            <!-- Licensing information: https://bootstrapmade.com/license/ -->
            <!-- Purchase the pro version with working PHP/AJAX contact form: [buy-url] -->
            Designed by <a href="https://bootstrapmade.com/">BootstrapMade</a>
        </div>
    </div>

</footer>
<script>
    //Lưu trữ các món ăn và số lượng
    var order = {};

    function addDish(dishName) {
        // Lấy phần tử textarea
        var orderTextArea = document.getElementById('orderTextArea'); // Sử dụng ID thay vì class để tránh nhầm lẫn

        alert("Đã chọn món: " + dishName); // In món ăn đã chọn ra console (nếu cần)

        // Kiểm tra món ăn đã có trong danh sách chưa
        if (order[dishName]) {
            order[dishName] += 1; // Nếu có rồi thì tăng số lượng
        } else {
            order[dishName] = 1; // Nếu chưa có, thêm món ăn mới vào
        }

        // Cập nhật lại ô yêu cầu với các món ăn và số lượng
        var orderText = '';
        for (var dish in order) {
            orderText += dish + ' - ' + order[dish] + ' x\n'; // Format: Món ăn - số lượng
        }
        orderTextArea.value = orderText; // Cập nhật nội dung vào textarea
    }

</script>

<!-- Scroll Top -->
<a href="#" id="scroll-top" class="scroll-top d-flex align-items-center justify-content-center"><i class="bi bi-arrow-up-short"></i></a>

<!-- Preloader -->
<div id="preloader"></div>

<!-- Vendor JS Files -->
<script src="assets/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>
<script src="assets/vendor/php-email-form/validate.js"></script>
<script src="assets/vendor/aos/aos.js"></script>
<script src="assets/vendor/glightbox/js/glightbox.min.js"></script>
<script src="assets/vendor/purecounter/purecounter_vanilla.js"></script>
<script src="assets/vendor/swiper/swiper-bundle.min.js"></script>

<!-- Main JS File -->
<script src="assets/js/main.js"></script>

</body>

</html>
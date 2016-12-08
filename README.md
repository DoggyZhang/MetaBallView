# MetaBallView
基于Bezier曲线(贝塞尔曲线)的仿QQ拖动消除消息小控件

<img src="https://github.com/DoggyZhang/MetaBallView/blob/master/GIF.gif" width="33.3%">


可以设置拉伸颜色已经拉伸内容

        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.mipmap.ic_launcher);
        metaBallView.setDragView(imageView);
        
        metaBallView.setColor(getResources().getColor(R.color.red));

参考：
https://github.com/chenupt/BezierDemo
https://github.com/xuyisheng/BezierArt

学习笔记
#### 第一题
1. 主要还是栈的深度是怎么算出来这个问题不太清楚

#### 第二题
1. 实现自定义的`ClassLoader`需要继承`ClassLoader`或者`URLClassLoader`
2. 主要重写父类的`findClass`方法，最后的返回还是用的父类的`defineClass`，主要的工作量就在于怎么找到这个类，以及怎么将二进制流正确的读出来
3. 写的时候遇到一个问题就是：调用方法的时候一直报错`object is not an instance of declaring class`，其实就是自己在写的时候把生成的`class`对象放进了`Method.invoke`里，导致失败，正确的做法是需要通过`class`对象新建一个实例，然后将这个实例传入
4. 流忘记关闭了

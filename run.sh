rm -r bin/*
cd src
echo "compiling..."
javac -d ../bin --module-path $1 --add-modules javafx.controls com/orangomango/simpleshooter/MainApplication.java
cd ../bin
echo "executing..."
java --module-path $1 --add-modules javafx.controls com.orangomango.simpleshooter.MainApplication

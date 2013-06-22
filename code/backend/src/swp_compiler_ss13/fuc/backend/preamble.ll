; C standard library functions
declare i32 @printf(i8* noalias nocapture, ...)
declare i32 @snprintf(i8* noalias nocapture, i32, i8* noalias nocapture, ...)
declare i8* @strncat(i8* noalias nocapture, i8* noalias nocapture, i32)
declare i32 @strlen(i8* noalias nocapture)
declare i8* @memcpy(i8* noalias nocapture, i8* noalias nocapture, i32)
declare i8* @calloc(i32,i32)

; Exception handling - Use C++ exceptions

;; C++ standard library functions
declare i8* @__cxa_allocate_exception(i32)
declare void @__cxa_throw(i8*,i8*,i8*)
declare i32 @__gxx_personality_v0(...)
declare i32 @llvm.eh.typeid.for(i8*) nounwind readnone
declare i8* @__cxa_begin_catch(i8*)
declare void @__cxa_end_catch()

;; C++ variable needed to create valid C++ exceptions
@_ZTVN10__cxxabiv117__class_type_infoE = external global i8*

;; List of standard exceptions

@.exception.Uncaught.format = constant [55 x i8] c"Program terminated by uncaught exception of type '%s'\0A\00"

;;; Division by zero
@.exception.DivisionByZero.name = constant [24 x i8] c"DivisionByZeroException\00"
%.exception.DivisionByZero.type.ir = type { i8 }
@.exception.DivisionByZero.type.cpp = constant { i8*, i8* } {
  i8* bitcast (i8** getelementptr inbounds (i8** @_ZTVN10__cxxabiv117__class_type_infoE, i64 2) to i8*),
  i8* getelementptr inbounds ([24 x i8]* @.exception.DivisionByZero.name, i32 0, i32 0) }

;;; Array out of bounds
@.exception.ArrayOutOfBounds.name = constant [26 x i8] c"ArrayOutOfBoundsException\00"
%.exception.ArrayOutOfBounds.type.ir = type { i8*, i32, i32 }
@.exception.ArrayOutOfBounds.type.cpp = constant { i8*, i8* } {
  i8* bitcast (i8** getelementptr inbounds (i8** @_ZTVN10__cxxabiv117__class_type_infoE, i64 2) to i8*),
  i8* getelementptr inbounds ([26 x i8]* @.exception.ArrayOutOfBounds.name, i32 0, i32 0) }
@.exception.ArrayOutOfBounds.format = constant [46 x i8] c"Array address: %p\0AArray length: %d\0AIndex: %d\0A\00"

; Standard functions

;; Convert a Long to a String
@.string_ltoa_format = private unnamed_addr constant [4 x i8] c"%ld\00"
define i8* @ltoa(i64) {
  ; ceil(log10(2^63-1)) = 19 => 19 digits + 1 NUL = buffer size 20
  %buffer = alloca i8, i32 20
  %format = getelementptr [4 x i8]* @.string_ltoa_format, i64 0, i64 0
  %"n-1" = call i32 (i8*, i32, i8*, ...)* @snprintf(i8* %buffer, i32 20, i8* %format, i64 %0)
  %n = add i32 %"n-1", 1
  %str = call i8* (i32,i32)* @calloc(i32 %n, i32 1)
  call i8* (i8*, i8*, i32)* @memcpy(i8* %str, i8* %buffer, i32 %n)
  ret i8* %str
}

;; Convert a Double to a String
@.string_dtoa_format = private unnamed_addr constant [3 x i8] c"%e\00"
define i8* @dtoa(double) {
  ; buffer size arbitrarily set to 64
  %buffer = alloca i8, i32 64
  %format = getelementptr [3 x i8]* @.string_dtoa_format, i64 0, i64 0
  %"n-1" = call i32 (i8*, i32, i8*, ...)* @snprintf(i8* %buffer, i32 64, i8* %format, double %0)
  %n = add i32 %"n-1", 1
  %str = call i8* (i32,i32)* @calloc(i32 %n, i32 1)
  call i8* (i8*, i8*, i32)* @memcpy(i8* %str, i8* %buffer, i32 %n)
  ret i8* %str
}

;; Convert a Boolean to a String
@.string_btoa_false = private unnamed_addr constant [6 x i8] c"false\00"
@.string_btoa_true = private unnamed_addr constant [5 x i8] c"true\00"
define i8* @btoa(i1) {
  br i1 %0, label %IfTrue, label %IfFalse
  IfTrue:
    %true = getelementptr [5 x i8]* @.string_btoa_true, i64 0, i64 0
    %str.true = call i8* (i32,i32)* @calloc(i32 5, i32 1)
    call i8* (i8*, i8*, i32)* @memcpy(i8* %str.true, i8* %true, i32 5)
    br label %End
  IfFalse:
    %false = getelementptr [6 x i8]* @.string_btoa_false, i64 0, i64 0
    %str.false = call i8* (i32,i32)* @calloc(i32 6, i32 1)
    call i8* (i8*, i8*, i32)* @memcpy(i8* %str.false, i8* %false, i32 6)
    br label %End
  End:
  %str = phi i8* [ %str.true, %IfTrue ], [ %str.false, %IfFalse ]
  ret i8* %str
}

;; Concatenate two Strings (without changing either)
define i8* @concat_string(i8*,i8*) {
  %lhs.length = call i32 (i8*)* @strlen(i8* %0)
  %rhs.length = call i32 (i8*)* @strlen(i8* %1)
  %"length-1" = add i32 %lhs.length, %rhs.length
  %length = add i32 %"length-1", 1
  %str = call i8* (i32,i32)* @calloc(i32 %length, i32 1)
  call i8* (i8*, i8*, i32)* @strncat(i8* %str, i8* %0, i32 %lhs.length)
  call i8* (i8*, i8*, i32)* @strncat(i8* %str, i8* %1, i32 %rhs.length)
  ret i8* %str
}

;; Divide two Longs, throw an appropriate exception for division by zero
define i64 @div_long(i64,i64) {
  %condition = icmp eq i64 %1, 0
  br i1 %condition, label %Zero, label %NonZero
  NonZero:
    %result = sdiv i64 %0, %1
    ret i64 %result
  Zero:
    %exception.content = alloca %.exception.DivisionByZero.type.ir
    %exception.instance = call i8* @__cxa_allocate_exception(i32 1)
    call void @__cxa_throw(i8* %exception.instance, i8* bitcast ({ i8*, i8* }* @.exception.DivisionByZero.type.cpp to i8*), i8* null) noreturn
    unreachable
}

;; Divide two Doubles, throw an appropriate exception for division by zero
define double @div_double(double,double) {
  %condition = fcmp oeq double %1, 0.0
  br i1 %condition, label %Zero, label %NonZero
  NonZero:
    %result = fdiv double %0, %1
    ret double %result
  Zero:
    %exception.content = alloca %.exception.DivisionByZero.type.ir
    %exception.instance = call i8* @__cxa_allocate_exception(i32 1)
    call void @__cxa_throw(i8* %exception.instance, i8* bitcast ({ i8*, i8* }* @.exception.DivisionByZero.type.cpp to i8*), i8* null) noreturn
    unreachable
}

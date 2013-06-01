declare i32 @printf(i8* noalias nocapture, ...)
declare i32 @snprintf(i8* noalias nocapture, i32, i8* noalias nocapture, ...)
declare i8* @strncat(i8* noalias nocapture, i8* noalias nocapture, i32)
declare i32 @strlen(i8* noalias nocapture)
declare i8* @memcpy(i8* noalias nocapture, i8* noalias nocapture, i32)
declare i8* @calloc(i32,i32)

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

@.string_btoa_false = private unnamed_addr constant [6 x i8] c"false\00"
@.string_btoa_true = private unnamed_addr constant [5 x i8] c"true\00"
define i8* @btoa(i8) {
  %condition = trunc i8 %0 to i1
  br i1 %condition, label %IfTrue, label %IfFalse
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

define i8* @strconcat(i8*,i8*) {
  %lhs.length = call i32 (i8*)* @strlen(i8* %0)
  %rhs.length = call i32 (i8*)* @strlen(i8* %1)
  %"length-1" = add i32 %lhs.length, %rhs.length
  %length = add i32 %"length-1", 1
  %str = call i8* (i32,i32)* @calloc(i32 %length, i32 1)
  call i8* (i8*, i8*, i32)* @strncat(i8* %str, i8* %0, i32 %lhs.length)
  call i8* (i8*, i8*, i32)* @strncat(i8* %str, i8* %1, i32 %rhs.length)
  ret i8* %str
}
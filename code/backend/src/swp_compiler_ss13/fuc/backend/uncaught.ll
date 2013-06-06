UncaughtException:
  %.exception = landingpad { i8*, i32 } personality i8* bitcast (i32 (...)* @__gxx_personality_v0 to i8*)
           catch i8* bitcast ({ i8*, i8* }* @.exception.DivisionByZero.type.cpp to i8*)
           catch i8* bitcast ({ i8*, i8* }* @.exception.ArrayOutOfBounds.type.cpp to i8*)
  %format = getelementptr [55 x i8]* @.exception.Uncaught.format, i64 0, i64 0
  %.exception.instance = extractvalue { i8*, i32 } %.exception, 0
  %.exception.type = extractvalue { i8*, i32 } %.exception, 1
  br label %isDivisionByZeroException

isDivisionByZeroException:
  %.exception.divbyzero = call i32 @llvm.eh.typeid.for(i8* bitcast ({ i8*, i8* }* @.exception.DivisionByZero.type.cpp to i8*)) nounwind
  %isDivByZero = icmp eq i32 %.exception.type, %.exception.divbyzero
  br i1 %isDivByZero, label %DivisionByZeroException, label %isArrayOutOfBoundsException

isArrayOutOfBoundsException:
  %.exception.arrayoutofbounds = call i32 @llvm.eh.typeid.for(i8* bitcast ({ i8*, i8*}* @.exception.ArrayOutOfBounds.type.cpp to i8*)) nounwind
  %isArrayOutOfBounds = icmp eq i32 %.exception.type, %.exception.arrayoutofbounds
  br i1 %isArrayOutOfBounds, label %ArrayOutOfBoundsException, label %UnknownException

UnknownException:
  resume { i8*, i32 } %.exception
  ret i64 1

DivisionByZeroException:
  %.divisionbyzero.name = getelementptr [24 x i8]* @.exception.DivisionByZero.name, i64 0, i64 0
  call i32 (i8*, ...)* @printf(i8* %format, i8* %.divisionbyzero.name)
  ret i64 1

ArrayOutOfBoundsException:
  %.arrayoutofbounds.name = getelementptr [26 x i8]* @.exception.ArrayOutOfBounds.name, i64 0, i64 0
  call i32 (i8*, ...)* @printf(i8* %format, i8* %.arrayoutofbounds.name)
  %.arrayoutofbounds.instance = call i8* @__cxa_begin_catch(i8* %.exception.instance) nounwind
  %.arrayoutofbounds.exception = bitcast i8* %.arrayoutofbounds.instance to %.exception.ArrayOutOfBounds.type.ir*
  %.arrayoutofbounds.struct = load %.exception.ArrayOutOfBounds.type.ir* %.arrayoutofbounds.exception
  %.arrayoutofbounds.array = extractvalue %.exception.ArrayOutOfBounds.type.ir %.arrayoutofbounds.struct, 0
  %.arrayoutofbounds.size = extractvalue %.exception.ArrayOutOfBounds.type.ir %.arrayoutofbounds.struct, 1
  %.arrayoutofbounds.index = extractvalue %.exception.ArrayOutOfBounds.type.ir %.arrayoutofbounds.struct, 2
  %.arrayoutofbounds.format = getelementptr [46 x i8]* @.exception.ArrayOutOfBounds.format, i64 0, i64 0
  call i32 (i8*, ...)* @printf(i8* %.arrayoutofbounds.format, i8* %.arrayoutofbounds.array, i32 %.arrayoutofbounds.size, i32 %.arrayoutofbounds.index)
  call void @__cxa_end_catch()
  ret i64 1

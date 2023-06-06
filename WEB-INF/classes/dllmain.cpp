#define _WIN32_DCOM
#include <jni.h>
#include<iostream>
#include <string>
#include <windows.h>
#include<cmath>
#include <wincred.h>
#include <taskschd.h>
#include <comdef.h>
#include "create.h"
#pragma comment(lib, "taskschd.lib")
using namespace std;
JNIEXPORT jint JNICALL Java_create_add(JNIEnv* env, jobject thisObj, jstring name, jstring date, jstring time, jint week, jint day, jint notspecs) {
	const char* rname = env->GetStringUTFChars(name, NULL);
	const char* dd = env->GetStringUTFChars(date, NULL);
	const char* tt = env->GetStringUTFChars(time, NULL);
	int wk = (int)week;
	int dy = (int)day;
	int notificationspecs = (int)notspecs;
	cout << rname << endl;
	cout << dd << endl;
	cout << tt << endl;
	cout << (int)week << endl;
	cout << (int)day << endl;
	cout << (int)notificationspecs << endl;


	HRESULT hr = CoInitializeEx(NULL, COINIT_MULTITHREADED);
	if (FAILED(hr))
	{
		printf("\nCoInitializeEx failed: %x", hr);
		return 0;
	}
	hr = CoInitializeSecurity(NULL, -1, NULL, NULL,
		RPC_C_AUTHN_LEVEL_PKT_PRIVACY,//data seen by sender and receiver
		RPC_C_IMP_LEVEL_IMPERSONATE,// to access files
		NULL, 0, NULL);

	if (FAILED(hr))
	{
		printf("\nCoInitializeSecurity failed: %x", hr);
		CoUninitialize();
		return 0;
	}
	ITaskService* pService = NULL;
	hr = CoCreateInstance(CLSID_TaskScheduler,// task Scheduler id
		NULL,
		CLSCTX_INPROC_SERVER,//create and manage object .dll
		IID_ITaskService,
		(void**)&pService);
	if (FAILED(hr))
	{
		printf("Failed to CoCreate an instance of the TaskService class: %x", hr);
		CoUninitialize();
		return 0;
	}

	//  Connect to the task service.connect to the computer.
	hr = pService->Connect(_variant_t(), _variant_t(),
		_variant_t(), _variant_t());
	if (FAILED(hr))
	{
		printf("ITaskService::Connect failed: %x", hr);
		pService->Release();
		CoUninitialize();
		return 0;
	}
	ITaskFolder* pRootFolder = NULL;//access the folder
	hr = pService->GetFolder(_bstr_t(L"\\"), &pRootFolder);


	if (FAILED(hr))
	{
		printf("Cannot get Root Folder pointer: %x", hr);
		CoUninitialize();
		return 0;
	}
	LPCWSTR wszTaskName;
	string nam = rname;
	wstring temp = wstring(nam.begin(), nam.end());
	wszTaskName = temp.c_str();
	//Execute the program
	wstring wstrExecutablePath = L"E:\\vs c++\\NotificationApp\\x64\\Debug\\NotificationApp.exe";
	pRootFolder->DeleteTask(_bstr_t(wszTaskName), 0);
	ITaskDefinition* pTask = NULL;
	hr = pService->NewTask(0, &pTask);

	pService->Release();  // COM clean up.  Pointer is no longer used.
	if (FAILED(hr))
	{
		printf("Failed to CoCreate an instance of the TaskService class: %x", hr);
		pRootFolder->Release();
		CoUninitialize();
		return 0;
	}
	ITriggerCollection* pTriggerCollection = NULL;
	hr = pTask->get_Triggers(&pTriggerCollection);
	if (FAILED(hr))
	{
		printf("\nCannot get trigger collection: %x", hr);
		pRootFolder->Release();
		pTask->Release();
		CoUninitialize();
		return 0;
	}
	if (notificationspecs == 1)
	{

		
		ITrigger* pTrigger = NULL;
		hr = pTriggerCollection->Create(TASK_TRIGGER_DAILY, &pTrigger);
		pTriggerCollection->Release();
		if (FAILED(hr))
		{
			printf("\nCannot create the trigger: %x", hr);
			pRootFolder->Release();
			pTask->Release();
			CoUninitialize();
			return 0;
		}

		IDailyTrigger* pDailyTrigger = NULL;
		hr = pTrigger->QueryInterface(
			IID_IDailyTrigger, (void**)&pDailyTrigger);
		pTrigger->Release();
		if (FAILED(hr))
		{
			printf("\nQueryInterface call on IDailyTrigger failed: %x", hr);
			pRootFolder->Release();
			pTask->Release();
			CoUninitialize();
			return 0;
		}

		hr = pDailyTrigger->put_Id(_bstr_t(L"Trigger1"));
		if (FAILED(hr))
			printf("\nCannot put trigger ID: %x", hr);
		string d = dd, t = tt;
		string start_Boundry = d + "T" + t;
		const char* c_toBstr = start_Boundry.c_str();
		BSTR start = _com_util::ConvertStringToBSTR(c_toBstr);
		hr = pDailyTrigger->put_StartBoundary(_bstr_t(start));
		if (FAILED(hr))
			printf("\nCannot put start boundary: %x", hr);
		string end_Boundry = "2099-12-30T23:59:59";
		const char* c_toBstr2 = end_Boundry.c_str();
		BSTR end = _com_util::ConvertStringToBSTR(c_toBstr2);
		hr = pDailyTrigger->put_EndBoundary(_bstr_t(end));
		if (FAILED(hr))
			printf("\nCannot put the end boundary: %x", hr);
		hr = pDailyTrigger->put_DaysInterval((short)1);
		//pDailyTrigger->Release();
		if (FAILED(hr))
		{
			printf("\nCannot put days interval: %x", hr);
			pRootFolder->Release();
			pDailyTrigger->Release();
			pTask->Release();
			CoUninitialize();
			return 0;
		}
	}
	else if (notificationspecs == 2)
	{
		ITrigger* pTrigger = NULL;
		hr = pTriggerCollection->Create(TASK_TRIGGER_TIME, &pTrigger);
		pTriggerCollection->Release();
		if (FAILED(hr))
		{
			printf("\nCannot create trigger: %x", hr);
			pRootFolder->Release();
			pTask->Release();
			CoUninitialize();
			return 0;
		}

		ITimeTrigger* pTimeTrigger = NULL;
		hr = pTrigger->QueryInterface(
			IID_ITimeTrigger, (void**)&pTimeTrigger);//point to same task
		pTrigger->Release();
		if (FAILED(hr))
		{
			printf("\nQueryInterface call failed for ITimeTrigger: %x", hr);
			pRootFolder->Release();
			pTask->Release();
			CoUninitialize();
			return 0;
		}

		hr = pTimeTrigger->put_Id(_bstr_t(L"Trigger1"));
		if (FAILED(hr))
			printf("\nCannot put trigger ID: %x", hr);
		string d = dd, t = tt;
		string start_Boundry = d + "T" + t;
		const char* c_toBstr = start_Boundry.c_str();
		BSTR start = _com_util::ConvertStringToBSTR(c_toBstr);
		hr = pTimeTrigger->put_StartBoundary(_bstr_t(start));
		if (FAILED(hr))
			printf("\nCannot put start boundary: %x", hr);
		string end_Boundry = "2099-12-30T23:59:59";
		const char* c_toBstr2 = end_Boundry.c_str();
		BSTR end = _com_util::ConvertStringToBSTR(c_toBstr2);
		hr = pTimeTrigger->put_EndBoundary(_bstr_t(end));
		if (FAILED(hr))
		{
			printf("\nCannot put the end boundary: %x", hr);
			pRootFolder->Release();
			pTask->Release();
			CoUninitialize();
			return 0;
		}
	}
	else if (notificationspecs == 3)
	{
		ITrigger* pTrigger = NULL;
		hr = pTriggerCollection->Create(TASK_TRIGGER_MONTHLY, &pTrigger);
		pTriggerCollection->Release();
		if (FAILED(hr))
		{
			printf("\nCannot create trigger: %x", hr);
			pRootFolder->Release();
			pTask->Release();
			CoUninitialize();
			return 0;
		}

		IMonthlyTrigger *pMonthlyTrigger = NULL;
		hr = pTrigger->QueryInterface(IID_IMonthlyTrigger, (void**)&pMonthlyTrigger);//point to same task
		
		if (FAILED(hr))
		{
			printf("\nQueryInterface call failed for IMonthTrigger: %x", hr);
			pRootFolder->Release();
			pTask->Release();
			CoUninitialize();
			return 0;
		}
		string d = dd, t = tt;
		string start_Boundry = d + "T" + t;
		const char* c_toBstr = start_Boundry.c_str();
		BSTR start = _com_util::ConvertStringToBSTR(c_toBstr);
		hr = pMonthlyTrigger->put_StartBoundary(_bstr_t(start));
		if (FAILED(hr))
			printf("\nCannot put start boundary: %x", hr);
		string end_Boundry = "2099-12-30T23:59:59";
		const char* c_toBstr2 = end_Boundry.c_str();
		BSTR end = _com_util::ConvertStringToBSTR(c_toBstr2);
		hr = pMonthlyTrigger->put_EndBoundary(_bstr_t(end));
		if (FAILED(hr))
		{
			printf("\nCannot put the end boundary: %x", hr);
			pRootFolder->Release();
			pTask->Release();
			CoUninitialize();
			return 0;
		}
		hr = pMonthlyTrigger->put_DaysOfMonth(pow(2, dy - 1));
		pMonthlyTrigger->Release();
		pTrigger->Release();
		if (FAILED(hr))
		{
			printf("Cannot put the Days of Month");
			pRootFolder->Release();
			pTask->Release();
			CoUninitialize();
			return 0;
		}
	}
	else if (notificationspecs == 4)
	{
		ITrigger* pTrigger = NULL;
		hr = pTriggerCollection->Create(TASK_TRIGGER_WEEKLY, &pTrigger);
		pTriggerCollection->Release();
		if (FAILED(hr))
		{
			printf("\nCannot create trigger: %x", hr);
			pRootFolder->Release();
			pTask->Release();
			CoUninitialize();
			return 0;
		}
		IWeeklyTrigger* pWeeklyTrigger = NULL;
		hr = pTrigger->QueryInterface(
			IID_IWeeklyTrigger, (void**)&pWeeklyTrigger);//point to same task
		pTrigger->Release();
		if (FAILED(hr))
		{
			printf("\nQueryInterface call failed for IWeeklyTrigger: %x", hr);
			pRootFolder->Release();
			pTask->Release();
			CoUninitialize();
			return 0;
		}
		string d = dd, t = tt;
		string start_Boundry = d + "T" + t;
		const char* c_toBstr = start_Boundry.c_str();
		BSTR start = _com_util::ConvertStringToBSTR(c_toBstr);
		hr = pWeeklyTrigger->put_StartBoundary(_bstr_t(start));
		if (FAILED(hr))
			printf("\nCannot put start boundary: %x", hr);
		string end_Boundry = "2099-12-30T23:59:59";
		const char* c_toBstr2 = end_Boundry.c_str();
		BSTR end = _com_util::ConvertStringToBSTR(c_toBstr2);
		hr = pWeeklyTrigger->put_EndBoundary(_bstr_t(end));
		if (FAILED(hr))
		{
			printf("\nCannot put the end boundary: %x", hr);
			pRootFolder->Release();
			pTask->Release();
			CoUninitialize();
			return 0;
		}
		hr = pWeeklyTrigger->put_DaysOfWeek((short)(pow(2, wk - 1)));
		pWeeklyTrigger->Release();
		if (FAILED(hr))
		{
			printf("\nCannot put the daysofWeek:%x", hr);
			pRootFolder->Release();
			pTask->Release();
			CoUninitialize();
			return 0;
		}
	}
	IActionCollection* pActionCollection = NULL;
	//  Get the task action collection pointer.
	hr = pTask->get_Actions(&pActionCollection);
	if (FAILED(hr))
	{
		printf("\nCannot get task collection pointer: %x", hr);
		pRootFolder->Release();
		pTask->Release();
		CoUninitialize();
		return 0;
	}

	//  Create the action, specifying that it is an executable action.
	IAction* pAction = NULL;
	hr = pActionCollection->Create(TASK_ACTION_EXEC, &pAction);
	pActionCollection->Release();
	if (FAILED(hr))
	{
		printf("\nCannot create action: %x", hr);
		pRootFolder->Release();
		pTask->Release();
		CoUninitialize();
		return 0;
	}

	IExecAction* pExecAction = NULL;
	hr = pAction->QueryInterface(
		IID_IExecAction, (void**)&pExecAction);
	pAction->Release();
	if (FAILED(hr))
	{
		printf("\nQueryInterface call failed for IExecAction: %x", hr);
		pRootFolder->Release();
		pTask->Release();
		CoUninitialize();
		return 0;
	}
	hr = pExecAction->put_Path(_bstr_t(wstrExecutablePath.c_str()));
	pExecAction->Release();
	if (FAILED(hr))
	{
		printf("\nCannot put the executable path: %x", hr);
		pRootFolder->Release();
		pTask->Release();
		CoUninitialize();
		return 0;
	}


	IRegisteredTask* pRegisteredTask = NULL;
	hr = pRootFolder->RegisterTaskDefinition(
		_bstr_t(wszTaskName),
		pTask,
		TASK_CREATE_OR_UPDATE,
		_variant_t(),
		_variant_t(),
		TASK_LOGON_INTERACTIVE_TOKEN,//computer local account psw 
		_variant_t(L""),
		&pRegisteredTask);
	if (FAILED(hr))
	{
		printf("\nError saving the Task : %x", hr);
		pRootFolder->Release();
		pTask->Release();
		CoUninitialize();
		return 0;
	}
	else
	{
		printf("\n Success! Task successfully registered. \n");

		//  Clean up
		pRootFolder->Release();
		pTask->Release();
		pRegisteredTask->Release();
		CoUninitialize();
	}
	env->ReleaseStringUTFChars(name, rname);
	env->ReleaseStringUTFChars(date, dd);
	env->ReleaseStringUTFChars(time, tt);
	return 1;
}



//g++ -I"%JRE_HOME%\include" -I"%JRE_HOME%\include\win32" -shared -o myjni.dll create.cpp




//x86_64-w64-mingw32-g++ -I"%JAVA_HOME%\include" -I"%JAVA_HOME%\include\win32" -shared -o hello.dll HelloJNI.cpp



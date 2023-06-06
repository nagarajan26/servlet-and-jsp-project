#include<Windows.h>
#include <CkMailMan.h>
#include <CkEmail.h>
#include<iostream>
#include<string>
#include<ctime>
#include "mysql_connection.h"
#include <cppconn/driver.h>
#include <cppconn/exception.h>
#include <cppconn/resultset.h>
#include <cppconn/statement.h>
#include<cppconn/resultset.h>
#include<cppconn/prepared_statement.h>
using namespace std;
using namespace sql;
int main()
{
	try
	{
		Driver* driver;
		Connection* con;
		Statement* stmt;
		PreparedStatement* ptmt;
		ResultSet* rs;
		time_t now = time(0);
		tm* current = localtime(&now);
		driver = get_driver_instance();
		con = driver->connect("tcp://127.0.0.1:3306", "root", "Naga26@123");
		con->setSchema("Reminder");
		stmt = con->createStatement();
		rs = stmt->executeQuery("select rtype,rname,rdesc,notspecs,dd,tt,wk,mon,email from table1");
		int rtype, notspecs;
		int hh, mm, ss, hh2, mm2, Month, month, wk, week, found = 0, c = 0;
		string rname, rdesc, dd, tt, email1;
		while (rs->next())
		{
			rtype = 0;
			notspecs = rs->getInt(4);
			dd = rs->getString(5);
			tt = rs->getString(6);
			char arr[100];
			for (int i = 0; i < sizeof(arr); i++)
			{
				arr[i] = tt[i];
			}
			char* token = strtok(arr, ":");
			char* array[3];
			int i = 0;
			while (token != NULL)
			{
				array[i] = token;
				token = strtok(NULL, ":");
				i++;
			}
			hh = stoi(array[0]);
			mm = stoi(array[1]);
			ss = stoi(array[2]);
			time_t now = time(0);
			tm* ltm = localtime(&now);
			hh2 = ltm->tm_hour;
			mm2 = ltm->tm_min;
			week = ltm->tm_wday;
			Month = ltm->tm_mday;
			//printf("%d\n%d\n%d\n%d\n%d\n", hh, mm, ss, hh2, mm2);
			if (notspecs == 1 && hh == hh2 && (mm == mm2 || mm2 == (mm + 1)))
			{
				rname = rs->getString(2);
				rdesc = rs->getString(3);
				rtype = rs->getInt(1);
				email1 = rs->getString(9);
				const char* em = email1.c_str();
				if (rtype == 2)
				{
					CkMailMan mailman;
					mailman.put_SmtpHost("smtp.gmail.com");

					mailman.put_SmtpUsername("nagarajan2000123@gmail.com");
					mailman.put_SmtpPassword("iexdtqsfbxwohjvu");

					mailman.put_SmtpSsl(true);
					mailman.put_SmtpPort(465);

					// Create a new email object
					CkEmail email;
					string desc = rdesc + "\n\n\n\nThanks&Regards\nNagarajan \nThis is the reminder for you";
					email.put_Subject(rname.c_str());
					email.put_Body(desc.c_str());
					email.put_From("Nagarajan.M <nagarajan2000123@gmail.com>");
					bool success = email.AddTo("nagarajan", em);

					success = mailman.SendEmail(email);
					if (success != true)
					{
						std::cout << mailman.lastErrorText() << "\r\n";
					}

					success = mailman.CloseSmtpConnection();
					if (success != true)
					{
						cout << "Connection to SMTP server not closed cleanly." << "\r\n";
					}

					cout << "Mail Sent!" << "\r\n";
					c = 1;
				}
				else if (rtype == 1)
				{
					wstring name = wstring(rname.begin(), rname.end());
					wstring desc = wstring(rdesc.begin(), rdesc.end());
					int msgbox = MessageBox(NULL,
						desc.c_str(),
						name.c_str(), MB_OK | MB_ICONINFORMATION);
					printf("Sucessghjkl");
					c = 1;
				}
			}
			else if (notspecs == 2 && hh == hh2 && (mm == mm2 || mm2 == (mm + 1)))
			{
				rname = rs->getString(2);
				rdesc = rs->getString(3);
				rtype = rs->getInt(1);
				email1 = rs->getString(9);
				const char* em = email1.c_str();
				found = 1;
				if (rtype == 2)
				{
					CkMailMan mailman;
					mailman.put_SmtpHost("smtp.gmail.com");

					mailman.put_SmtpUsername("nagarajan2000123@gmail.com");
					mailman.put_SmtpPassword("iexdtqsfbxwohjvu");

					mailman.put_SmtpSsl(true);
					mailman.put_SmtpPort(465);

					// Create a new email object
					CkEmail email;
					string desc = rdesc + "\n\n\n\nThanks&Regards\nNagarajan \nThis is the reminder for you";
					email.put_Subject(rname.c_str());
					email.put_Body(desc.c_str());
					email.put_From("Nagarajan.M <nagarajan2000123@gmail.com>");
					bool success = email.AddTo("nagarajan", em);

					success = mailman.SendEmail(email);
					if (success != true)
					{
						std::cout << mailman.lastErrorText() << "\r\n";
					}

					success = mailman.CloseSmtpConnection();
					if (success != true)
					{
						cout << "Connection to SMTP server not closed cleanly." << "\r\n";
					}

					cout << "Mail Sent!" << "\r\n";
					found = 1;
				}
				else if (rtype == 1)
				{
					wstring name = wstring(rname.begin(), rname.end());
					wstring desc = wstring(rdesc.begin(), rdesc.end());
					int msgbox = MessageBox(NULL,
						desc.c_str(),
						name.c_str(), MB_OK | MB_ICONINFORMATION);
					printf("Sucessghjkl");
					found = 1;
				}
			}
			else if (notspecs == 3 && hh == hh2 && (mm == mm2 || mm2 == (mm + 1)))
			{
				month = rs->getInt(8);
				if (month == Month)
				{
					rname = rs->getString(2);
					rdesc = rs->getString(3);
					rtype = rs->getInt(1);
					email1 = rs->getString(9);
					const char* em = email1.c_str();
					if (rtype == 2)
					{
						CkMailMan mailman;
						mailman.put_SmtpHost("smtp.gmail.com");

						mailman.put_SmtpUsername("nagarajan2000123@gmail.com");
						mailman.put_SmtpPassword("iexdtqsfbxwohjvu");

						mailman.put_SmtpSsl(true);
						mailman.put_SmtpPort(465);

						// Create a new email object
						CkEmail email;
						string desc = rdesc + "\n\n\n\nThanks&Regards\nNagarajan \nThis is the reminder for you";
						email.put_Subject(rname.c_str());
						email.put_Body(desc.c_str());
						email.put_From("Nagarajan.M <nagarajan2000123@gmail.com>");
						bool success = email.AddTo("nagarajan", em);

						success = mailman.SendEmail(email);
						if (success != true)
						{
							std::cout << mailman.lastErrorText() << "\r\n";
						}

						success = mailman.CloseSmtpConnection();
						if (success != true)
						{
							cout << "Connection to SMTP server not closed cleanly." << "\r\n";
						}

						cout << "Mail Sent!" << "\r\n";
						c = 1;
					}
					else if (rtype == 1)
					{
						wstring name = wstring(rname.begin(), rname.end());
						wstring desc = wstring(rdesc.begin(), rdesc.end());
						int msgbox = MessageBox(NULL,
							desc.c_str(),
							name.c_str(), MB_OK | MB_ICONINFORMATION);
						printf("Sucessghjkl");
						c = 1;
					}
				}
			}
			else if (notspecs == 4 && hh == hh2 && (mm == mm2 || mm2 == (mm + 1)))
			{
				wk = rs->getInt(7);
				if (wk == (week + 1))
				{
					rname = rs->getString(2);
					rdesc = rs->getString(3);
					rtype = rs->getInt(1);
					email1 = rs->getString(9);
					const char* em = email1.c_str();
					if (rtype == 2)
					{
						CkMailMan mailman;
						mailman.put_SmtpHost("smtp.gmail.com");

						mailman.put_SmtpUsername("nagarajan2000123@gmail.com");
						mailman.put_SmtpPassword("iexdtqsfbxwohjvu");

						mailman.put_SmtpSsl(true);
						mailman.put_SmtpPort(465);

						// Create a new email object
						CkEmail email;
						string desc = rdesc + "\n\n\n\nThanks&Regards\nNagarajan \nThis is the reminder for you";
						email.put_Subject(rname.c_str());
						email.put_Body(desc.c_str());
						email.put_From("Nagarajan.M <nagarajan2000123@gmail.com>");
						bool success = email.AddTo("nagarajan", em);

						success = mailman.SendEmail(email);
						if (success != true)
						{
							std::cout << mailman.lastErrorText() << "\r\n";
						}

						success = mailman.CloseSmtpConnection();
						if (success != true)
						{
							cout << "Connection to SMTP server not closed cleanly." << "\r\n";
						}

						cout << "Mail Sent!" << "\r\n";
						c = 1;
					}
					else if (rtype == 1)
					{
						wstring name = wstring(rname.begin(), rname.end());
						wstring desc = wstring(rdesc.begin(), rdesc.end());
						int msgbox = MessageBox(NULL,
							desc.c_str(),
							name.c_str(), MB_OK | MB_ICONINFORMATION);
						printf("Sucessghjkl");
						c = 1;
					}
				}
			}
		}
		cout << "1";
		if (c == 0)
		{
			cout << "No Reminders";
		}
		if (found == 1)
		{
			ptmt = con->prepareStatement("delete from table1 where rname=?");
			ptmt->setString(1, rname);
		}
	}
	catch (SQLException& e)
	{
		cout << e.what();
	}
}

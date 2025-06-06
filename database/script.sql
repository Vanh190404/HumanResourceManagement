USE [master]
GO
/****** Object:  Database [HumanManangement]    Script Date: 5/21/2025 9:16:49 PM ******/
CREATE DATABASE [HumanManangement]
 CONTAINMENT = NONE
 ON  PRIMARY 
( NAME = N'HumanManangement', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL16.SQLEXPRESS\MSSQL\DATA\HumanManangement.mdf' , SIZE = 8192KB , MAXSIZE = UNLIMITED, FILEGROWTH = 65536KB )
 LOG ON 
( NAME = N'HumanManangement_log', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL16.SQLEXPRESS\MSSQL\DATA\HumanManangement_log.ldf' , SIZE = 8192KB , MAXSIZE = 2048GB , FILEGROWTH = 65536KB )
 WITH CATALOG_COLLATION = DATABASE_DEFAULT, LEDGER = OFF
GO
ALTER DATABASE [HumanManangement] SET COMPATIBILITY_LEVEL = 160
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [HumanManangement].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [HumanManangement] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [HumanManangement] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [HumanManangement] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [HumanManangement] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [HumanManangement] SET ARITHABORT OFF 
GO
ALTER DATABASE [HumanManangement] SET AUTO_CLOSE OFF 
GO
ALTER DATABASE [HumanManangement] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [HumanManangement] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [HumanManangement] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [HumanManangement] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [HumanManangement] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [HumanManangement] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [HumanManangement] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [HumanManangement] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [HumanManangement] SET  DISABLE_BROKER 
GO
ALTER DATABASE [HumanManangement] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [HumanManangement] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [HumanManangement] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [HumanManangement] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [HumanManangement] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [HumanManangement] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [HumanManangement] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [HumanManangement] SET RECOVERY SIMPLE 
GO
ALTER DATABASE [HumanManangement] SET  MULTI_USER 
GO
ALTER DATABASE [HumanManangement] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [HumanManangement] SET DB_CHAINING OFF 
GO
ALTER DATABASE [HumanManangement] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF ) 
GO
ALTER DATABASE [HumanManangement] SET TARGET_RECOVERY_TIME = 60 SECONDS 
GO
ALTER DATABASE [HumanManangement] SET DELAYED_DURABILITY = DISABLED 
GO
ALTER DATABASE [HumanManangement] SET ACCELERATED_DATABASE_RECOVERY = OFF  
GO
ALTER DATABASE [HumanManangement] SET QUERY_STORE = ON
GO
ALTER DATABASE [HumanManangement] SET QUERY_STORE (OPERATION_MODE = READ_WRITE, CLEANUP_POLICY = (STALE_QUERY_THRESHOLD_DAYS = 30), DATA_FLUSH_INTERVAL_SECONDS = 900, INTERVAL_LENGTH_MINUTES = 60, MAX_STORAGE_SIZE_MB = 1000, QUERY_CAPTURE_MODE = AUTO, SIZE_BASED_CLEANUP_MODE = AUTO, MAX_PLANS_PER_QUERY = 200, WAIT_STATS_CAPTURE_MODE = ON)
GO
USE [HumanManangement]
GO
/****** Object:  User [jv_user1]    Script Date: 5/21/2025 9:16:49 PM ******/
CREATE USER [jv_user1] FOR LOGIN [jv_user1] WITH DEFAULT_SCHEMA=[dbo]
GO
/****** Object:  Table [dbo].[Attendance]    Script Date: 5/21/2025 9:16:49 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Attendance](
	[Id] [int] IDENTITY(1,1) NOT NULL,
	[EmployeeId] [int] NOT NULL,
	[Present] [bit] NOT NULL,
	[Overtime] [int] NULL,
	[WorkDays] [int] NULL,
	[AbsentDays] [int] NULL,
 CONSTRAINT [PK__Attendan__3214EC07A134087A] PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Department]    Script Date: 5/21/2025 9:16:49 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Department](
	[Id] [int] IDENTITY(1,1) NOT NULL,
	[Name] [nvarchar](100) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Employee]    Script Date: 5/21/2025 9:16:49 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Employee](
	[Id] [int] IDENTITY(1,1) NOT NULL,
	[FullName] [nvarchar](200) NOT NULL,
	[Gender] [nvarchar](10) NULL,
	[DateOfBirth] [date] NOT NULL,
	[PhoneNumber] [nvarchar](20) NULL,
	[DepartmentId] [int] NOT NULL,
	[PositionId] [int] NOT NULL,
	[StartDate] [date] NOT NULL,
	[EmploymentType] [nvarchar](50) NULL,
	[BankName] [nvarchar](100) NULL,
	[AccountNumber] [nvarchar](50) NULL,
	[Branch] [nvarchar](100) NULL,
PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[LeaveRequest]    Script Date: 5/21/2025 9:16:49 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[LeaveRequest](
	[Id] [int] IDENTITY(1,1) NOT NULL,
	[EmployeeId] [int] NOT NULL,
	[FromDate] [date] NOT NULL,
	[ToDate] [date] NOT NULL,
	[Reason] [nvarchar](max) NULL,
	[Status] [nvarchar](20) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Position]    Script Date: 5/21/2025 9:16:49 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Position](
	[Id] [int] IDENTITY(1,1) NOT NULL,
	[Title] [nvarchar](100) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Report]    Script Date: 5/21/2025 9:16:49 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Report](
	[Id] [int] IDENTITY(1,1) NOT NULL,
	[Type] [nvarchar](50) NULL,
	[EmployeeId] [int] NULL,
	[KPI] [bit] NULL,
PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[SalaryRecord]    Script Date: 5/21/2025 9:16:49 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[SalaryRecord](
	[Id] [int] IDENTITY(1,1) NOT NULL,
	[EmployeeId] [int] NOT NULL,
	[Month] [int] NOT NULL,
	[Year] [int] NOT NULL,
	[BaseSalary] [decimal](18, 2) NOT NULL,
	[Bonus] [decimal](18, 2) NULL,
	[Deductions] [decimal](18, 2) NULL,
	[NetSalary]  AS (([BaseSalary]+[Bonus])-[Deductions]),
	[totalWorkHours] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
ALTER TABLE [dbo].[Attendance] ADD  CONSTRAINT [DF__Attendanc__Prese__403A8C7D]  DEFAULT ((0)) FOR [Present]
GO
ALTER TABLE [dbo].[LeaveRequest] ADD  DEFAULT ('Pending') FOR [Status]
GO
ALTER TABLE [dbo].[SalaryRecord] ADD  DEFAULT ((0)) FOR [Bonus]
GO
ALTER TABLE [dbo].[SalaryRecord] ADD  DEFAULT ((0)) FOR [Deductions]
GO
ALTER TABLE [dbo].[Employee]  WITH CHECK ADD FOREIGN KEY([DepartmentId])
REFERENCES [dbo].[Department] ([Id])
GO
ALTER TABLE [dbo].[Employee]  WITH CHECK ADD FOREIGN KEY([PositionId])
REFERENCES [dbo].[Position] ([Id])
GO
ALTER TABLE [dbo].[LeaveRequest]  WITH CHECK ADD FOREIGN KEY([EmployeeId])
REFERENCES [dbo].[Employee] ([Id])
GO
ALTER TABLE [dbo].[Report]  WITH CHECK ADD  CONSTRAINT [FK_Report_Employee] FOREIGN KEY([EmployeeId])
REFERENCES [dbo].[Employee] ([Id])
GO
ALTER TABLE [dbo].[Report] CHECK CONSTRAINT [FK_Report_Employee]
GO
ALTER TABLE [dbo].[SalaryRecord]  WITH CHECK ADD FOREIGN KEY([EmployeeId])
REFERENCES [dbo].[Employee] ([Id])
GO
ALTER TABLE [dbo].[Report]  WITH CHECK ADD CHECK  (([Type]='Leave' OR [Type]='Salary' OR [Type]='Attendance'))
GO
ALTER TABLE [dbo].[SalaryRecord]  WITH CHECK ADD CHECK  (([Month]>=(1) AND [Month]<=(12)))
GO
USE [master]
GO
ALTER DATABASE [HumanManangement] SET  READ_WRITE 
GO
